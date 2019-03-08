/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 19:57.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component;

import javax.swing.*;

public class StatusPane extends JTextPane {

    private static final String FPS = " fps";

    private String fileName;
    private String fps;

    public StatusPane() {
        setEditable(false);
        setOpaque(false);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;

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
