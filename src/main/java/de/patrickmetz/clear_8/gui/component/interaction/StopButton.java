/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.component.interaction;

import javax.swing.*;

final public class StopButton extends JButton {

    private static final String TEXT_STOP = "Stop";

    public StopButton() {
        setEnabled(false);
        setFocusable(false);

        setText(TEXT_STOP);
    }

}
