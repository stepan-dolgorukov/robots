package ru.urfu.gui;

import ru.urfu.controller.RobotController;
import ru.urfu.model.RobotInfo;
import ru.urfu.model.RobotModel;
import ru.urfu.serialization.Saveable;
import ru.urfu.serialization.State;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyVetoException;
import java.util.Observable;
import java.util.Observer;

public class RobotCoordinatesFrame extends JInternalFrame implements Observer
        , Saveable {

    final JLabel coordsLabel;
    private RobotInfo robotInfo_;
    public RobotCoordinatesFrame(final RobotController robotController) {
        setTitle("Координаты робота");
        coordsLabel = new JLabel();
        coordsLabel.setVisible(true);
        add(coordsLabel);
        robotController.addSubscriber(this);
        robotController.requestModelInfo();
    }

    private void onModelUpdate() {
        Point2D position = robotInfo_.getPosition();
        String coordsText = String.format("%d %d", (int)position.getX(),
                (int)position.getY());
        coordsLabel.setText(coordsText);
    }

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
        onModelUpdate();
    }

    @Override
    public String getName() {
        return "RobotCoordinatesFrame";
    }

    @Override
    public State state() {
        final State state = new State();

        {
            state.setProperty("name", getName());
        }

        {
            final Point location = getLocation();
            state.setProperty("X", location.x);
            state.setProperty("Y", location.y);
        }

        {
            final Dimension dimension = getSize();
            state.setProperty("width", dimension.width);
            state.setProperty("height", dimension.height);
        }

        {
            final boolean hidden = isIcon();
            state.setProperty("hidden", hidden);
        }

        return state;
    }

    @Override
    public void setState(State state) {
        if (null == state) {
            return;
        }

        setSize(
                (int)state.getProperty("width"),
                (int)state.getProperty("height"));

        setLocation(
                (int)state.getProperty("X"),
                (int)state.getProperty("Y"));

        if ((boolean)state.getProperty("hidden")) {
            try {
                setIcon(true);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
