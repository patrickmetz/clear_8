/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 19:58.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component;

import javax.swing.*;

public class CpuComboBox extends JComboBox<String> {

    public final static String CPU_COSMAC_VIP = "Cosmac VIP";
    public final static String CPU_SUPER_CHIP = "Super Chip";

    public CpuComboBox() {
        setEditable(false);
        setEnabled(false);

        addItem(CPU_SUPER_CHIP);
        addItem(CPU_COSMAC_VIP);
    }

}
