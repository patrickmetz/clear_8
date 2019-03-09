/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 16:35.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.gui.component.interaction.CpuComboBox;
import de.patrickmetz.bean8.runner.Runner;

import java.awt.event.ItemEvent;

public class CpuComboBoxListener implements java.awt.event.ItemListener {

    private final Runner runner;

    public CpuComboBoxListener(Runner runner) {
        this.runner = runner;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            runner.setLegacyMode(
                    e.getItem() == CpuComboBox.CPU_VIP
            );
        }
    }

}