/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 10.03.19 16:14.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.output;

import de.patrickmetz.bean8.runner.Runner;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerState;

import javax.swing.*;
import java.io.File;

public class StatusPane extends JTextPane implements RunnerEventListener {

    private static final String TEXT_FPS = " fps";

    private String fileName;
    private String fps;

    public StatusPane() {
        setEditable(false);
        setOpaque(false);

        setFocusable(false);
    }

    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        RunnerState status = e.getState();

        if (status == RunnerState.STARTED) {
            Runner runner = (Runner) e.getSource();

            setFileName(
                    new File(runner.getRomPath()).getName()
            );
        } else if (status == RunnerState.STOPPED) {
            fps = fileName = null;
            updateText();
        }
    }

    public void setFps(String fps) {
        this.fps = fps;

        updateText();
    }

    private boolean containsDot(String fileName) {
        return fileName.indexOf('.') > -1;
    }

    private String lowercaseWithoutExtension(String fileName) {
        return fileName.split("\\.")[0].toLowerCase();
    }

    private void setFileName(String fileName) {
        if ((fileName != null) && containsDot(fileName)) {
            this.fileName = lowercaseWithoutExtension(fileName);
        }

        updateText();
    }

    private void updateText() {
        String text = "";

        if (fileName != null) {
            text = fileName;
        }

        if (fps != null) {
            text += " | " + fps + TEXT_FPS;
        }

        setText(text);
    }

}
