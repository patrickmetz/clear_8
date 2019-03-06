/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 06.03.19 21:42.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.action.FpsTimerAction;
import de.patrickmetz.bean8.gui.action.LoadGameButtonAction;
import de.patrickmetz.bean8.gui.action.PauseButtonAction;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Gui {

    private static Runner runner;

    private JPanel bottomPanel;
    private JPanel contentPane;
    private JTextPane fpsPane;
    private Timer fpsTimer;
    private JPanel menuPanel;
    private JButton pauseButton;
    private Screen screen;
    private JPanel screenArea;
    private JButton selectRomButton;
    private JTextPane statusPane;

    private Gui() {
        setSelectRomButtonListener();
        setRunButtonListener();

        prepareStatusPane();
        prepareScreen();
        prepareRunButton();

        prepareFpsTimer();
    }

    /**
     * Creates the Graphical User Interface (GUI) and keeps it responsive by putting it on Swings
     * Event Dispatch Thread (EDT).
     * <p>
     * see: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
     */
    public static void render(Runner runner) {
        Gui.runner = runner;

        SwingUtilities.invokeLater(() -> {
            prepareGui();

            if (runner.getRomPath() != null) {
                runner.run();
            }
        });
    }

    private static JFrame createWindow(JPanel contentPane) {
        JFrame window = new JFrame("bean8");
        window.setContentPane(contentPane);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();

        return window;
    }

    private static void prepareGui() {
        Gui gui = new Gui();

        prepareWindow(
                createWindow(
                        gui.contentPane
                )
        );
    }

    private static void prepareWindow(JFrame window) {
        window.setLocationRelativeTo(null); // centers window
        window.setResizable(false);
        window.setVisible(true);
    }

    private void prepareFpsTimer() {
        fpsTimer = new Timer(
                1000,
                new FpsTimerAction(screen, fpsPane)
        );

        fpsTimer.start();
    }

    private void prepareRunButton() {
        if (!runner.getRomPath().isBlank()) {
            pauseButton.setEnabled(true);
        }
    }

    private void prepareScreen() {
        screenArea.setPreferredSize(new Dimension(640, 480));
        screen = new Screen();

        screenArea.add(screen);
        runner.setScreen(screen);
    }

    private void prepareStatusPane() {
        String romPath = runner.getRomPath();

        if (romPath != null) {
            statusPane.setText(
                    new File(romPath).getName()
            );
        }
    }

    private void setRunButtonListener() {
        pauseButton.addActionListener(
                new PauseButtonAction(runner)
        );
    }

    private void setSelectRomButtonListener() {
        selectRomButton.addActionListener(
                new LoadGameButtonAction(runner, statusPane, pauseButton)
        );
    }

}
