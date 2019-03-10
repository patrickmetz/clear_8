/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 10.03.19 16:14.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.gui.component.interaction.StopButton;
import de.patrickmetz.bean8.runner.Runner;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopButtonListener implements ActionListener, RunnerEventListener {

    private final Runner runner;
    private final StopButton stopButton;

    public StopButtonListener(Runner runner, StopButton stopButton) {
        this.runner = runner;
        this.stopButton = stopButton;
    }

    /**
     * handles mouse clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        runner.stop();
    }

    /**
     * handles runner state changes
     */
    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        RunnerState state = e.getState();

        if (state == RunnerState.STARTED) {
            stopButton.setEnabled(true);
        } else if (state == RunnerState.STOPPED) {
            stopButton.setEnabled(false);
        }
    }

}