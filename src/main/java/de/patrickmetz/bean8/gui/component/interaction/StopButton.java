/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 20:22.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;

public class StopButton extends JButton {

    public StopButton() {
        setEnabled(false);
        setText("Stop");
    }
}
