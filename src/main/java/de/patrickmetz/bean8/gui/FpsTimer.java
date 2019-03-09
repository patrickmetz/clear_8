/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 15:29.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.gui.component.output.Display;
import de.patrickmetz.bean8.gui.component.output.StatusPane;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerStatus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FpsTimer implements RunnerEventListener {

    private final Display display;
    private final StatusPane statusPane;

    private final Timer timer;

    FpsTimer(Display display, StatusPane statusPane) {
        this.display = display;
        this.statusPane = statusPane;

        timer = new Timer(
                1000,
                new FpsTimerListener()
        );
    }

    @Override
    public void handleRunnerEvent(RunnerEvent event) {
        RunnerStatus status = event.getStatus();

        if (status == RunnerStatus.STARTED) {
            timer.start();
        } else if (status == RunnerStatus.STOPPED) {
            timer.stop();
        }
    }

    private class FpsTimerListener implements ActionListener {

        private int updates;

        @Override
        public void actionPerformed(ActionEvent e) {
            int newUpdates = display.getUpdateCount();
            statusPane.setFps("" + (newUpdates - updates));
            updates = newUpdates;
        }

    }

}
