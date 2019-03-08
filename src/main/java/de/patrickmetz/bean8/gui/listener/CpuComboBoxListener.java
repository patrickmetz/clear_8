/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 19:59.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.component.CpuComboBox;

import java.awt.event.ItemEvent;

public class CpuComboBoxListener implements java.awt.event.ItemListener {

    private final Runner runner;

    public CpuComboBoxListener(Runner runner) {
        this.runner = runner;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {
            Object item = e.getItem();

            if (item == CpuComboBox.CPU_COSMAC_VIP) {
                runner.setLegacyMode(true);
            } else if (item == CpuComboBox.CPU_SUPER_CHIP) {
                runner.setLegacyMode(false);
            }
        }
    }

}
