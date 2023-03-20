package ru.urfu.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import org.json.JSONObject;
import ru.urfu.log.Logger;
import ru.urfu.serialization.*;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить
 * отдельный класс).
 */
public class MainApplicationFrame extends JFrame implements Saveable {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final File storeFile =
            new File(System.getProperty("user.home").concat("/.robots"));

    public MainApplicationFrame() {
        // Make the big window be indented 50 pixels from each edge
        // of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2,
            screenSize.height - inset * 2);

        setContentPane(desktopPane);
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                onExitPress();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setStates();
    }

    private void setStates() {
        final StateLoader loader = new FileStateLoader(storeFile);
        final Map<String, State> states = loader.load();

        if (null == states) {
            return;
        }

        for (final JInternalFrame frame : desktopPane.getAllFrames()) {
            final String name = ((Saveable)frame).getName();
            if (states.containsKey(name)) {
                ((Saveable) frame).setState(states.get(name));
            }
        }
        setState(states.get(((Saveable)this).getName()));
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    //    protected JMenuBar createMenuBar() {
    //        JMenuBar menuBar = new JMenuBar();
    //
    //        //Set up the lone menu.
    //        JMenu menu = new JMenu("Document");
    //        menu.setMnemonic(KeyEvent.VK_D);
    //        menuBar.add(menu);
    //
    //        //Set up the first menu item.
    //        JMenuItem menuItem = new JMenuItem("New");
    //        menuItem.setMnemonic(KeyEvent.VK_N);
    //        menuItem.setAccelerator(KeyStroke.getKeyStroke(
    //                KeyEvent.VK_N, ActionEvent.ALT_MASK));
    //        menuItem.setActionCommand("new");
    ////        menuItem.addActionListener(this);
    //        menu.add(menuItem);
    //
    //        //Set up the second menu item.
    //        menuItem = new JMenuItem("Quit");
    //        menuItem.setMnemonic(KeyEvent.VK_Q);
    //        menuItem.setAccelerator(KeyStroke.getKeyStroke(
    //                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
    //        menuItem.setActionCommand("quit");
    ////        menuItem.addActionListener(this);
    //        menu.add(menuItem);
    //
    //        return menuBar;
    //    }
    private JMenuBar generateMenuBar() {
        final JMenuBar menuBar = new JMenuBar();

        for (final AbstractButton item : generateMenuBarItems()) {
            menuBar.add(item);
        }

        return menuBar;
    }

    /**
     * Создание всех элементов менюбара.
     *
     * @return список элементов
     */
    private List<AbstractButton> generateMenuBarItems() {
        return Arrays.asList(createLookAndFeelMenu(), createTestsMenu(),
            createProgramClosingButton());
    }

    /**
     * Создает меню выбора стиля пользовательского интерфейса программы.
     *
     * @return объект-меню
     */
    private JMenu createLookAndFeelMenu() {
        final JMenu menu = new JMenu("Режим отображения");

        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
            "Управление режимом отображения приложения");

        {
            final JMenuItem systemLookAndFeel =
                new JMenuItem("Системная схема");

            systemLookAndFeel.setMnemonic(KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });

            menu.add(systemLookAndFeel);
        }

        {
            final JMenuItem lookAndFeel = new JMenuItem("Универсальная схема");

            lookAndFeel.setMnemonic(KeyEvent.VK_S);
            lookAndFeel.addActionListener((event) -> {
                setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });

            menu.add(lookAndFeel);
        }

        return menu;
    }

    /**
     * Создаёт меню c тестами.
     *
     * @return объект-меню
     */
    private JMenu createTestsMenu() {
        final JMenu menu = new JMenu("Тесты");

        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(
            "Тестовые команды");

        final JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог");

        addLogMessageItem.setMnemonic(KeyEvent.VK_S);
        addLogMessageItem.addActionListener(
            (event) -> { Logger.debug("Новая строка"); });

        menu.add(addLogMessageItem);
        return menu;
    }

    /**
     * Показ пользователю диалога подтверждения выхода.
     */
    private void onExitPress() {
        final String[] options = {"Так точно", "Никак нет"};

        final int exit =
            JOptionPane.showOptionDialog(this, "Вы точно хотите выйти?",
                "Подтверждение", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options,

                // стандартно подсвечивается кнопка подтверждения
                options[0]);

        if (JOptionPane.YES_OPTION == exit) {
            final StateSaver saver = new FileStateSaver(storeFile);
            saver.save(getAllToSave());
            System.exit(0);
        }
    }

    List<Saveable> getAllToSave() {
        List<Saveable> objectsToSave = new LinkedList<>();
        for (final var frame : desktopPane.getAllFrames()) {
            objectsToSave.add((Saveable)frame);
        }
        objectsToSave.add(this);
        return objectsToSave;
    }

    /**
     * Создаёт меню выхода из программы.
     *
     * @return объект-кнопка
     */
    private JButton createProgramClosingButton() {
        final JButton button = new JButton("Выход");

        button.setMnemonic(KeyEvent.VK_Q);
        button.addActionListener(
            (event)
                -> Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        return button;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    /**
     * Получить состояние окошка MainApplicationFrame.
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
            final boolean hidden = (JFrame.ICONIFIED == getState());
            state.setProperty("hidden", hidden);
        }

        return state;
    }

    /**
     * Уникальное имя окошка.
     */
    @Override
    public String getName() {
        return "MainApplicationFrame";
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
    }
}