/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 20:24.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.action.RunButtonAction;
import de.patrickmetz.bean8.gui.action.SelectRomButtonAction;

import javax.swing.*;
import java.awt.*;

public class Gui {

    private JPanel bottomPanel;
    private JPanel contentPane;
    private JPanel menuPanel;
    private JButton runButton;
    private JPanel screenArea;
    private JButton selectRomButton;
    private JTextPane statusPane;

    private Gui(Runner runner) {
        setSelectRomButtonListener(runner, this);
        setRunButtonListener(runner);
        prepareScreen(runner);
    }

    public static void render(Runner runner) {
        // using invokeLater prevents deadlocks and race conditions
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                prepareWindow(
                        createWindow(runner)
                );
            }
        });
    }

    public JTextPane getStatusPane() {
        return statusPane;
    }

    private static JFrame createWindow(Runner runner) {
        JFrame window = new JFrame("bean8");
        window.setContentPane(new Gui(runner).contentPane);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();

        return window;
    }

    private static void prepareWindow(JFrame window) {
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
    }

    private void prepareScreen(Runner runner) {
        screenArea.setPreferredSize(new Dimension(640, 480));
        Screen screen = new Screen();

        screenArea.add(screen);
        runner.setScreen(screen);
    }

    private void setRunButtonListener(Runner runner) {
        runButton.addActionListener(
                new RunButtonAction(runner)
        );
    }

    private void setSelectRomButtonListener(Runner runner, Gui gui) {
        selectRomButton.addActionListener(
                new SelectRomButtonAction(runner, gui)
        );
    }

}
