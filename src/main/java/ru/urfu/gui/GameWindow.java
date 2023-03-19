package ru.urfu.gui;

import ru.urfu.serialization.Saveable;
import ru.urfu.serialization.State;

import java.awt.*;

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
}
