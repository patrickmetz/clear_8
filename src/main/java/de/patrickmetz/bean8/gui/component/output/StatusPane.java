/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 16:17.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.output;

import de.patrickmetz.bean8.runner.Runner;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerStatus;

import javax.swing.*;
import java.io.File;

public class StatusPane extends JTextPane implements RunnerEventListener {

    private static final String FPS = " fps";

    private String fileName;
    private String fps;

    public StatusPane() {
        setEditable(false);
        setOpaque(false);
    }

    @Override
    public void handleRunnerEvent(RunnerEvent event) {
        RunnerStatus status = event.getStatus();

        if (status == RunnerStatus.STARTED) {
            Runner runner = (Runner) event.getSource();

            setFileName(
                    new File(runner.getRomPath()).getName()
            );
        } else if (status == RunnerStatus.STOPPED) {
            setText("");
        }
    }

    public void setFps(String fps) {
        this.fps = fps;

        updateText();
    }

    private void setFileName(String fileName) {
        if (fileName != null) {
            this.fileName = fileName.split("\\.")[0].toLowerCase();
        }

        updateText();
    }

    private void updateText() {
        String status = "";

        if ((fileName != null) && (fps != null)) {
            status = fileName + " | " + fps + FPS;
        }

        setText(status);
    }

}
