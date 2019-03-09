/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 16:28.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerStatus;

import javax.swing.*;

public class StopButton extends JButton implements RunnerEventListener {

    private static final String TEXT_STOP = "Stop";

    public StopButton() {
        setEnabled(false);
        setText(TEXT_STOP);
    }

    @Override
    public void handleRunnerEvent(RunnerEvent event) {
        RunnerStatus status = event.getStatus();

        if (status == RunnerStatus.STARTED) {
            setEnabled(true);
        } else if (status == RunnerStatus.STOPPED) {
            setEnabled(false);
        }
    }

}
