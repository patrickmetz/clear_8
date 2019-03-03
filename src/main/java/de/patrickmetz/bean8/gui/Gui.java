/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 16:00.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.action.RunAction;
import de.patrickmetz.bean8.gui.action.SelectRomAction;

import javax.swing.*;
import java.awt.*;

public class Gui {

    private JPanel bottomPanel;
    private JPanel contentPane;
    private JPanel menuPanel;
    private JButton runButton;
    private JPanel screen;
    private JButton selectRomButton;
    private JTextPane statusPane;

    private Gui(Runner runner) {
        setSelectRomButtonListener(runner, this);
        setRunButtonListener(runner);
        prepareScreen();
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

    public JTextPane getStatusPane() {
        return statusPane;
    }

    private void prepareScreen() {
        screen.setPreferredSize(new Dimension(640, 480));
        screen.setBackground(new Color(255, 255, 255));
    }

    private void setRunButtonListener(Runner runner) {
        runButton.addActionListener(
                new RunAction(runner)
        );
    }

    private void setSelectRomButtonListener(Runner runner, Gui gui) {
        selectRomButton.addActionListener(
                new SelectRomAction(runner, gui)
        );
    }

}
