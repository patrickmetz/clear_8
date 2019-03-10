/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 10.03.19 16:14.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.gui.component.interaction.PauseButton;
import de.patrickmetz.bean8.runner.Runner;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseButtonListener implements ActionListener, RunnerEventListener {

    private final PauseButton pauseButton;
    private final Runner runner;

    public PauseButtonListener(Runner runner, PauseButton pauseButton) {
        this.runner = runner;
        this.pauseButton = pauseButton;
    }

    /**
     * handles mouse clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        runner.togglePause();
    }

    /**
     * handles runner states
     */
    @Override
    public void handleRunnerEvent(RunnerEvent e) {

        switch (e.getState()) {
            case STARTED:
                pauseButton.setEnabled(true);
                break;

            case STOPPED:
                pauseButton.setEnabled(false);
                break;

            case PAUSED:
                pauseButton.setText(PauseButton.TEXT_RESUME);
                break;

            case RESUMED:
                pauseButton.setText(PauseButton.TEXT_PAUSE);
                break;
        }
    }

}