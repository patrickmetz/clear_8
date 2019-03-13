/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.component.interaction;

import javax.swing.*;

final public class CpuComboBox extends JComboBox<String> {

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

    public void setSelectedItem(Object value) {
        super.setSelectedItem(
                (boolean) value ? CPU_VIP : CPU_SCHIP
        );
    }

}
