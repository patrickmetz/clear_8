/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 13:36.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component;

import javax.swing.*;

public class StopButton extends JButton {

    public StopButton() {
        setEnabled(false);
        setText("Stop");
    }
}
