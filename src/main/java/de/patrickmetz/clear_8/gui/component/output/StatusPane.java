/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.component.output;

import javax.swing.*;

final public class StatusPane extends JTextPane {

    private static final String TEXT_FPS = " fps";

    private String fileName;
    private String fps;

    public StatusPane() {
        setEditable(false);
        setOpaque(false);

        setFocusable(false);
    }

    public void updateFileName(String fileName) {
        if ((fileName != null) && containsDot(fileName)) {
            this.fileName = lowercaseWithoutExtension(fileName);
        } else {
            this.fileName = null;
        }

        updateText();
    }

    public void updateFps(String fps) {
        this.fps = fps;

        updateText();
    }

    private boolean containsDot(String fileName) {
        return fileName.indexOf('.') > -1;
    }

    private String lowercaseWithoutExtension(String fileName) {
        return fileName.split("\\.")[0].toLowerCase();
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
