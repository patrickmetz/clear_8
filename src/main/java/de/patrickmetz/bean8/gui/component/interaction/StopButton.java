/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 11.03.19 13:33.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;

public class StopButton extends JButton {

    private static final String TEXT_STOP = "Stop";

    public StopButton() {
        setEnabled(false);
        setFocusable(false);

        setText(TEXT_STOP);
    }

}
