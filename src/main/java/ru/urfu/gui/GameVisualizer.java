package ru.urfu.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import javax.swing.*;

import ru.urfu.controller.RobotController;
import ru.urfu.model.RobotInfo;
import ru.urfu.model.RobotModel;

public class GameVisualizer extends JPanel implements Observer {
    private final Timer m_timer = initTimer();
    
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private RobotController controller_;
    private RobotInfo robotInfo_;

    public GameVisualizer(RobotController controller)
    {
        controller_ = controller;
        controller_.addSubscriber(this);
        controller_.requestModelInfo();

        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (null == robotInfo_) {
                    return;
                }
                controller_.requestModelUpdate(robotInfo_.getTargetPosition());
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                final Point point = e.getPoint();
                controller_.requestModelUpdate(new Point2D.Double(point.getX(),
                                point.getY()));
//                robotModel.update(e.getX(), e.getY());
//                System.err.println(String.format("%d %d", e.getX(), e.getY()));
            }
        });
        setDoubleBuffered(true);
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    protected void onModelUpdateEvent()
    {
        onRedrawEvent();
    }
    private static int round(double value)
    {
        return (int)(value + 0.5);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d);
        drawTarget(g2d);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    private void drawRobot(Graphics2D g)
    {
        if (robotInfo_ == null) {
            return;
        }
        Point2D.Double position = robotInfo_.getPosition();

        int robotCenterX = round(position.getX());
        int robotCenterY = round(position.getY());
        AffineTransform t =
                AffineTransform.getRotateInstance(robotInfo_.getDirection(),
                robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }
    
    private void drawTarget(Graphics2D g)
    {
        if (robotInfo_ == null) {
            return;
        }

        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);

        Point2D.Double targetPosition =
                robotInfo_.getTargetPosition();

        int x = round(targetPosition.getX());
        int y = round(targetPosition.getY());
//        System.err.println(String.format("%d %d", x, y));
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    /**
     * Метод, сообщающий об обновлении observable-объекта.
     * Метод вызывается автоматически.
     */
    @Override
    public void update(final Observable observable, final Object update) {
        if (null == observable) {
            return;
        }

        if (null == update) {
            return;
        }

        if (!(observable instanceof RobotModel)) {
            return;
        }

        if (!(update instanceof RobotInfo)) {
            return;
        }

        robotInfo_ = (RobotInfo) update;
        onModelUpdateEvent();
    }
}
