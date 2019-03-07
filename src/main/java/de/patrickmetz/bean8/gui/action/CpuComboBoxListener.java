/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 07.03.19 22:08.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class CpuComboBoxListener implements java.awt.event.ItemListener {

    public final static String TEXT_COSMAC_VIP = "Cosmac VIP";
    public final static String TEXT_SUPER_CHIP = "Super Chip";

    private final Runner runner;
    private String selectedItem;

    public CpuComboBoxListener(Runner runner) {
        this.runner = runner;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Object item = e.getItem();

            if (item == TEXT_COSMAC_VIP) {
                runner.setLegacyMode(true);
            } else if (item == TEXT_SUPER_CHIP) {
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
