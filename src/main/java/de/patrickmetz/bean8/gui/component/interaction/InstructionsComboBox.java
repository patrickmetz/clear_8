/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 12:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;

public class InstructionsComboBox extends JComboBox<Integer> {

    private static final int MAXIMUM = 1000;
    private static final int MINIMUM = 100;
    private static final int STEP = 100;

    private static final String TOOL_TIP = "CPU instructions per second";

    public InstructionsComboBox() {
        setEditable(false);
        setFocusable(false);

        setToolTipText(TOOL_TIP);

        for (int value = MINIMUM; value <= MAXIMUM; value += STEP) {
            addItem(value);
        }
    }

    public void updateSelection(int value) {
        super.setSelectedItem(value);
    }

}
