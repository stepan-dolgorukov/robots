package ru.urfu.gui;

import ru.urfu.serialization.Saveable;
import ru.urfu.serialization.State;

import java.awt.*;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements Saveable
{
    private final GameVisualizer m_visualizer;
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    /**
     * Получить состояние окна GameWindow.
     */
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

    /**
     * Уникальное имя окна.
     */
    @Override
    public String getName() {
        return "GameWindow";
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
