/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 13:59.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.gui.component.interaction.StopButton;
import de.patrickmetz.clear_8.runner.Runner;
import de.patrickmetz.clear_8.runner.event.RunnerEvent;
import de.patrickmetz.clear_8.runner.event.RunnerEventListener;
import de.patrickmetz.clear_8.runner.event.RunnerState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopButtonListener implements ActionListener, RunnerEventListener {

    private final StopButton button;
    private final Runner runner;

    public StopButtonListener(Runner runner, StopButton button) {
        this.runner = runner;
        this.button = button;
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
            button.setEnabled(true);
        } else if (state == RunnerState.STOPPED) {
            button.setEnabled(false);
        }
    }

}