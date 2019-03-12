/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 13:59.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.component.interaction;

import javax.swing.*;

public class LoadRomButton extends JButton {

    private static final String TEXT_LOAD_ROM = "Load ROM";

    public LoadRomButton() {
        setEnabled(true);
        setText(TEXT_LOAD_ROM);

        setFocusable(false);
    }

}
