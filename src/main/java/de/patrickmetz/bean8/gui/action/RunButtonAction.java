/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 05.03.19 16:51.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunButtonAction implements ActionListener {

    private static final String TEXT_RUNNING = "Stop";
    private static final String TEXT_STOPPED = "Run";

    private final Runner runner;

    private boolean isRunning = false;

    public RunButtonAction(Runner runner) {
        this.runner = runner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (isRunning) {
            runner.stop();
            isRunning = false;
            button.setText(TEXT_STOPPED);
        } else {
            runner.run();
            isRunning = true;
            button.setText(TEXT_RUNNING);
        }

    }
}
