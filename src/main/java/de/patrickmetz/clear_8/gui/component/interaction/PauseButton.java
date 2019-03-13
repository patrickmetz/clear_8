/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.component.interaction;

import javax.swing.*;

final public class PauseButton extends JButton {

    private static final String TEXT_PAUSE = "Pause";
    private static final String TEXT_RESUME = "Resume";

    public PauseButton() {
        setEnabled(false);
        setFocusable(false);

        usePauseText();
    }

    public void usePauseText() {
        setText(TEXT_PAUSE);
    }

    public void useResumeText() {
        setText(TEXT_RESUME);
    }

}
