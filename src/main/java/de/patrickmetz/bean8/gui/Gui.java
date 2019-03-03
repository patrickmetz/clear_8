/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 14:07.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.action.RunAction;
import de.patrickmetz.bean8.gui.action.SelectRomAction;

import javax.swing.*;

public class Gui {

    private JButton runButton;
    private JButton selectROMButton;
    private JPanel window;

    private Gui(Runner runner) {
        prepareSelectRomButton(runner);
        prepareRunButton(runner);
    }

    public static void render(Runner runner) {
        JFrame frame = new JFrame("Gui");
        frame.setContentPane(new Gui(runner).window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void prepareRunButton(Runner runner) {
        runButton.addActionListener(
                new RunAction(runner)
        );
    }

    private void prepareSelectRomButton(Runner runner) {
        selectROMButton.addActionListener(
                new SelectRomAction(runner)
        );
    }

}
