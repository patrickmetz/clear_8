/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 21:41.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import javax.swing.*;

public class CpuComboBox extends JComboBox<String> {

    public final static String CPU_SCHIP = "Super Chip";
    public final static String CPU_VIP = "Cosmac VIP";

    public CpuComboBox() {
        setEditable(false);

        addItem(CPU_SCHIP);
        addItem(CPU_VIP);

        setFocusable(false);
    }

}
