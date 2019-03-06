/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 06.03.19 16:30.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseButtonAction implements ActionListener {

    private static final String TEXT_IS_PAUSED = "Resume";
    private static final String TEXT_IS_RUNNING = "Pause";

    private final Runner runner;

    public PauseButtonAction(Runner runner) {
        this.runner = runner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (runner.isRunning()) {
            if (runner.isPaused()) {
                button.setText(TEXT_IS_RUNNING);
            } else {
                button.setText(TEXT_IS_PAUSED);
            }
        } else {
            return;
        }

        runner.togglePause();
    }
}
