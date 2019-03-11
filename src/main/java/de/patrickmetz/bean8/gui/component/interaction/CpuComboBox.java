/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 11.03.19 13:42.
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

}
