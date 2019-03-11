/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 11.03.19 13:26.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.gui.component.output.StatusPane;
import de.patrickmetz.bean8.runner.Runner;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerState;

import java.io.File;

public class StatusPaneListener implements RunnerEventListener {

    private final StatusPane pane;

    public StatusPaneListener(StatusPane pane) {
        this.pane = pane;
    }

    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        RunnerState status = e.getState();

        if (status == RunnerState.STARTED) {
            Runner runner = (Runner) e.getSource();

            pane.updateFileName(
                    new File(runner.getRomPath()).getName()
            );
        } else if (status == RunnerState.STOPPED) {
            pane.updateFileName(null);
            pane.updateFps(null);
        }
    }

}