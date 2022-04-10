package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.event.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.event.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.event.RunnerState;
import de.patrickmetz.clear_8.gui.component.interaction.CpuComboBox;

import java.awt.event.ItemEvent;

final public class CpuComboBoxListener implements java.awt.event.ItemListener, EmulatorEventListener {

    private final CpuComboBox comboBox;
    private final Emulator    emulator;

    public CpuComboBoxListener(Emulator emulator, CpuComboBox comboBox) {
        this.emulator = emulator;
        this.comboBox = comboBox;
    }

    @Override
    public void handleRunnerEvent(EmulatorEvent e) {
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
            emulator.setUseVipCpu(
                    e.getItem() == CpuComboBox.CPU_VIP
            );
        }
    }

}