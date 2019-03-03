/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 14:54.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.action.RunAction;
import de.patrickmetz.bean8.gui.action.SelectRomAction;

import javax.swing.*;
import java.awt.*;

public class Gui {

    private JPanel panel;
    private JButton runButton;
    private JPanel screen;
    private JButton selectROMButton;

    private Gui(Runner runner) {
        prepareSelectRomButton(runner);
        prepareRunButton(runner);
        prepareScreen();
    }

    public static void render(Runner runner) {
        // enables swing to update the gui asynchronously
        // and prevent deadlocks and race conditions
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createWindow(runner);
            }
        });
    }

    private static void createWindow(Runner runner) {
        JFrame window = new JFrame("bean8");
        window.setContentPane(new Gui(runner).panel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }

    private void prepareRunButton(Runner runner) {
        runButton.addActionListener(
                new RunAction(runner)
        );
    }

    private void prepareScreen() {
        screen.setPreferredSize(new Dimension(640, 480));
        screen.setBackground(new Color(255, 255, 255));
    }

    private void prepareSelectRomButton(Runner runner) {
        selectROMButton.addActionListener(
                new SelectRomAction(runner)
        );
    }

}
