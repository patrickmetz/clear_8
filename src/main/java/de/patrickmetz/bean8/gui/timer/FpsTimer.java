/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 11.03.19 12:15.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.timer;

import de.patrickmetz.bean8.gui.component.output.Display;
import de.patrickmetz.bean8.gui.component.output.StatusPane;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FpsTimer implements RunnerEventListener {

    private static final int DELAY = 1000;

    private final StatusPane statusPane;
    private final Timer timer;

    private Display display;

    public FpsTimer(Display display, StatusPane statusPane) {
        this.display = display;
        this.statusPane = statusPane;

        timer = new Timer(
                DELAY,
                new FpsTimerListener()
        );
    }

    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        RunnerState status = e.getState();

        if (status == RunnerState.STARTED) {
            timer.start();
        } else if (status == RunnerState.STOPPED) {
            timer.stop();
        }
    }

    public void setDisplay(Display display) {
        this.display = display;

    }

    private class FpsTimerListener implements ActionListener {

        private int updates;

        @Override
        public void actionPerformed(ActionEvent e) {
            int newUpdates = display.getUpdateCount();
            statusPane.updateFps("" + (newUpdates - updates));
            updates = newUpdates;
        }

    }

}
