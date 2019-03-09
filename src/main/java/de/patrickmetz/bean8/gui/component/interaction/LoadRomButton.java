/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 16:27.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;

public class LoadRomButton extends JButton {

    private static final String TEXT_LOAD_ROM = "Load ROM";

    public LoadRomButton() {
        setEnabled(true);
        setText(TEXT_LOAD_ROM);
    }

}
