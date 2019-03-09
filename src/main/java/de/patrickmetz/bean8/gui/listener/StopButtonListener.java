/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 17:35.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.gui.component.interaction.StopButton;
import de.patrickmetz.bean8.runner.Runner;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerStatus;

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
     * handles events created by mouse clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        runner.stop();
    }

    /**
     * handles events created by the runner
     */
    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        RunnerStatus status = e.getStatus();

        if (status == RunnerStatus.STARTED) {
            stopButton.setEnabled(true);
        } else if (status == RunnerStatus.STOPPED) {
            stopButton.setEnabled(false);
        }
    }

}