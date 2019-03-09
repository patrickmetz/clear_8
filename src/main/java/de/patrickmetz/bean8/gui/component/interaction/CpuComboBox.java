/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 14:50.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.component.interaction;

import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerStatus;

import javax.swing.*;

public class CpuComboBox extends JComboBox<String> implements RunnerEventListener {

    public final static String CPU_SCHIP = "Super Chip";
    public final static String CPU_VIP = "Cosmac VIP";

    public CpuComboBox() {
        setEditable(false);

        addItem(CPU_SCHIP);
        addItem(CPU_VIP);
    }

    @Override
    public void handleRunnerEvent(RunnerEvent event) {
        RunnerStatus status = event.getStatus();

        if (status == RunnerStatus.STARTED) {
            setEnabled(false);
        } else if (status == RunnerStatus.STOPPED) {
            setEnabled(true);
        }
    }

}
