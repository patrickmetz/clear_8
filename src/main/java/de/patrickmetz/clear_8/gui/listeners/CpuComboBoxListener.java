package de.patrickmetz.clear_8.gui.listeners;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.events.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.events.EmulatorState;
import de.patrickmetz.clear_8.globals.Text;
import de.patrickmetz.clear_8.gui.elements.CpuComboBox;

import java.awt.event.ItemEvent;

final public class CpuComboBoxListener implements java.awt.event.ItemListener, EmulatorEventListener {

    private final CpuComboBox comboBox;
    private final Emulator    emulator;

    public CpuComboBoxListener(Emulator emulator, CpuComboBox comboBox) {
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
            emulator.setUseVipCpu(
                    e.getItem() == Text.Gui.CPU_VIP
            );
        }
    }

}