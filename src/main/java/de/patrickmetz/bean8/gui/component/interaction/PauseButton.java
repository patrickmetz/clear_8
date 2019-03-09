/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 14:56.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerStatus;

import javax.swing.*;

public class PauseButton extends JButton implements RunnerEventListener {

    private static final String TEXT_PAUSE = "Pause";
    private static final String TEXT_RESUME = "Resume";

    public PauseButton() {
        setEnabled(false);
        setText(TEXT_PAUSE);
    }

    @Override
    public void handleRunnerEvent(RunnerEvent event) {
        RunnerStatus status = event.getStatus();

        if (status == RunnerStatus.STARTED) {
            setEnabled(true);
        } else if (status == RunnerStatus.STOPPED) {
            setEnabled(false);
        } else if (status == RunnerStatus.PAUSED) {
            setText(TEXT_RESUME);
        } else if (status == RunnerStatus.RESUMED) {
            setText(TEXT_PAUSE);
        }
    }

}
