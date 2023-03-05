package ru.urfu.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import ru.urfu.log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить
 * отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
            createProgramClosingMenu());
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
     * Обработка нажатия кнопки выхода.
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
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Создаёт меню выхода из программы.
     *
     * @return объект-кнопка
     */
    private JButton createProgramClosingMenu() {
        final JButton button = new JButton("Выход");

        button.setMnemonic(KeyEvent.VK_Q);
        button.addActionListener((event) -> onExitPress());

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
}
