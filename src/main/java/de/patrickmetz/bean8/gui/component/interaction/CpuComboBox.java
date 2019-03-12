/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 12:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;

public class CpuComboBox extends JComboBox<String> {

    public final static String CPU_SCHIP = "Super Chip";
    public final static String CPU_VIP = "Cosmac VIP";

    private static final String TOOL_TIP = "CPU type";

    public CpuComboBox() {
        setEditable(false);
        setFocusable(false);

        setToolTipText(TOOL_TIP);

        addItem(CPU_SCHIP);
        addItem(CPU_VIP);
    }

    public void updateSelection(boolean value) {
        super.setSelectedItem(
                value ? CPU_VIP : CPU_SCHIP
        );
    }

}
