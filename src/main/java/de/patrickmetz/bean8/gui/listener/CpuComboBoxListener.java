/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 17:08.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.gui.component.interaction.CpuComboBox;
import de.patrickmetz.bean8.runner.Runner;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerStatus;

import java.awt.event.ItemEvent;

public class CpuComboBoxListener implements java.awt.event.ItemListener, RunnerEventListener {

    private final CpuComboBox cpuComboBox;
    private final Runner runner;

    public CpuComboBoxListener(Runner runner, CpuComboBox cpuComboBox) {
        this.runner = runner;
        this.cpuComboBox = cpuComboBox;
    }

    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        RunnerStatus status = e.getStatus();

        if (status == RunnerStatus.STARTED) {
            cpuComboBox.setEnabled(false);
        } else if (status == RunnerStatus.STOPPED) {
            cpuComboBox.setEnabled(true);
        }
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