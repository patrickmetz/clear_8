/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 07.03.19 18:35.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.Gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopButtonAction implements ActionListener {

    private final Timer fpsTimer;
    private final Gui gui;
    private final JButton pauseButton;
    private final Runner runner;

    public StopButtonAction(Runner runner, JButton pauseButton, Gui gui, Timer fpsTimer) {
        this.runner = runner;
        this.pauseButton = pauseButton;
        this.gui = gui;

        this.fpsTimer = fpsTimer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton stopButton = (JButton) e.getSource();

        if (runner.isRunning()) {
            runner.stop();
            gui.resetScreen();
            gui.resetFpsTimer();

            stopButton.setEnabled(false);
            pauseButton.setEnabled(false);

        } else {
            stopButton.setEnabled(true);
            pauseButton.setEnabled(true);
        }

    }
}
