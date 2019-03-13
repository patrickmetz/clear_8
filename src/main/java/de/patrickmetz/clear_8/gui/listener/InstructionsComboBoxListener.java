/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.gui.component.interaction.InstructionsComboBox;
import de.patrickmetz.clear_8.runner.Runner;
import de.patrickmetz.clear_8.runner.event.RunnerEvent;
import de.patrickmetz.clear_8.runner.event.RunnerEventListener;
import de.patrickmetz.clear_8.runner.event.RunnerState;

import java.awt.event.ItemEvent;

final public class InstructionsComboBoxListener implements java.awt.event.ItemListener, RunnerEventListener {

    private final InstructionsComboBox comboBox;
    private final Runner runner;

    public InstructionsComboBoxListener(Runner runner, InstructionsComboBox comboBox) {
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
            runner.setInstructionsPerSecond(
                    (int) e.getItem()
            );
        }
    }

}