/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 13:36.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.component.CpuComboBox;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class CpuComboBoxListener implements java.awt.event.ItemListener {

    private final Runner runner;
    private String selectedItem;

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
            } else {
                JComboBox<String> comboBox = (JComboBox<String>) e.getItemSelectable();

                if (selectedItem != null) {
                    comboBox.setSelectedItem(selectedItem);
                }
            }

            selectedItem = (String) item;
        }
    }
}
