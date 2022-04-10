package de.patrickmetz.clear_8.gui.listeners;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.events.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.events.EmulatorState;
import de.patrickmetz.clear_8.gui.elements.InstructionsComboBox;

import java.awt.event.ItemEvent;

final public class InstructionsComboBoxListener implements java.awt.event.ItemListener, EmulatorEventListener {

    private final InstructionsComboBox comboBox;
    private final Emulator             emulator;

    public InstructionsComboBoxListener(Emulator emulator, InstructionsComboBox comboBox) {
        this.emulator = emulator;
        this.comboBox = comboBox;
    }

    @Override
    public void handleEmulatorEvent(EmulatorEvent e) {
        EmulatorState state = e.getState();

        if (state == EmulatorState.STARTED) {
            comboBox.setEnabled(false);
        } else if (state == EmulatorState.STOPPED) {
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