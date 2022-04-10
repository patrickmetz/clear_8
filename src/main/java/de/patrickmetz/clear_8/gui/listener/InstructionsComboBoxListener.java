package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.event.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.event.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.event.RunnerState;
import de.patrickmetz.clear_8.gui.component.interaction.InstructionsComboBox;

import java.awt.event.ItemEvent;

final public class InstructionsComboBoxListener implements java.awt.event.ItemListener, EmulatorEventListener {

    private final InstructionsComboBox comboBox;
    private final Emulator             emulator;

    public InstructionsComboBoxListener(Emulator emulator, InstructionsComboBox comboBox) {
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
            emulator.setInstructionsPerSecond(
                    (int) e.getItem()
            );
        }
    }

}