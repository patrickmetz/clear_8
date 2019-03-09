/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 12:15.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;

public class PauseButton extends JToggleButton {

    public PauseButton() {
        setEnabled(false);
        setText("Pause");
    }

}
