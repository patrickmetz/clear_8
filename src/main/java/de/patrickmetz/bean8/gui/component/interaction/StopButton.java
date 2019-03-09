/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 16:48.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;

public class StopButton extends JButton {

    private static final String TEXT_STOP = "Stop";

    public StopButton() {
        setEnabled(false);
        setText(TEXT_STOP);
    }

}
