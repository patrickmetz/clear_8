/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 11.03.19 13:31.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.gui.component.interaction.CpuComboBox;
import de.patrickmetz.bean8.runner.Runner;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerState;

import java.awt.event.ItemEvent;

public class CpuComboBoxListener implements java.awt.event.ItemListener, RunnerEventListener {

    private final CpuComboBox comboBox;
    private final Runner runner;

    public CpuComboBoxListener(Runner runner, CpuComboBox comboBox) {
        this.runner = runner;
        this.comboBox = comboBox;
    }

    /**
     * handles runner state changes
     */
    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        RunnerState state = e.getState();

        if (state == RunnerState.STARTED) {
            comboBox.setEnabled(false);
        } else if (state == RunnerState.STOPPED) {
            comboBox.setEnabled(true);
        }
    }

    /**
     * handles mouse clicks
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            runner.setUseVipCpu(
                    e.getItem() == CpuComboBox.CPU_VIP
            );
        }
    }

}