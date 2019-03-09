/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 16:47.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;

public class PauseButton extends JButton {

    public static final String TEXT_PAUSE = "Pause";
    public static final String TEXT_RESUME = "Resume";

    public PauseButton() {
        setEnabled(false);
        setText(TEXT_PAUSE);
    }

}
