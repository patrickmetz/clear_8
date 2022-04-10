package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.event.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.event.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.event.RunnerState;
import de.patrickmetz.clear_8.gui.component.interaction.StopButton;

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
    public void handleRunnerEvent(EmulatorEvent e) {
        RunnerState state = e.getState();

        if (state == RunnerState.STARTED) {
            button.setEnabled(true);
        } else if (state == RunnerState.STOPPED) {
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