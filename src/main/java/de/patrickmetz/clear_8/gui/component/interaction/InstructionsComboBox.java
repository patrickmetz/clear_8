/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.component.interaction;

import javax.swing.*;

final public class InstructionsComboBox extends JComboBox<Integer> {

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

    @Override
    public void setSelectedItem(Object value) {
        super.setSelectedItem(value);
    }

}
