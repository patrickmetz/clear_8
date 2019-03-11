/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 11.03.19 14:07.
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

    private final PauseButton button;
    private final Runner runner;

    public PauseButtonListener(Runner runner, PauseButton button) {
        this.runner = runner;
        this.button = button;
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
                button.setEnabled(true);
                button.usePauseText();
                break;

            case STOPPED:
                button.setEnabled(false);
                button.usePauseText();
                break;

            case PAUSED:
                button.useResumeText();
                break;

            case RESUMED:
                button.usePauseText();
                break;
        }
    }

}