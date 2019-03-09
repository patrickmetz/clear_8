/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 12:03.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.output;

import javax.swing.*;

public class StatusPane extends JTextPane {

    private static final String FPS = " fps";

    private String fileName;
    private String fps;

    public StatusPane() {
        setEditable(false);
        setOpaque(false);
    }

    public void clear() {
        setText("");
    }

    public void setFileName(String fileName) {
        if (fileName != null) {
            this.fileName = fileName.split("\\.")[0].toLowerCase();
        }

        updateText();
    }

    public void setFps(String fps) {
        this.fps = fps;

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
