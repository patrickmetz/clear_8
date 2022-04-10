package de.patrickmetz.clear_8.gui.listeners;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.events.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.events.EmulatorState;
import de.patrickmetz.clear_8.gui.elements.StopButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class StopButtonListener implements ActionListener, EmulatorEventListener {

    private final StopButton button;
    private final Emulator   emulator;

    public StopButtonListener(Emulator emulator, StopButton button) {
        this.emulator = emulator;
        this.button = button;
    }


    @Override
    public void handleEmulatorEvent(EmulatorEvent e) {
        EmulatorState state = e.getState();

        if (state == EmulatorState.STARTED) {
            button.setEnabled(true);
        } else if (state == EmulatorState.STOPPED) {
            button.setEnabled(false);
        }
    }

    /**
     * handles mouse clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        emulator.stop();
    }
}