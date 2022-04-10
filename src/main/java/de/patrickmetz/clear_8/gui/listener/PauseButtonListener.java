package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.event.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.event.EmulatorEventListener;
import de.patrickmetz.clear_8.gui.component.interaction.PauseButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class PauseButtonListener implements ActionListener, EmulatorEventListener {

    private final PauseButton button;
    private final Emulator    emulator;

    public PauseButtonListener(Emulator emulator, PauseButton button) {
        this.emulator = emulator;
        this.button = button;
    }

    @Override
    public void handleRunnerEvent(EmulatorEvent e) {
        switch (e.getState()) {
            case STARTED:
                button.setEnabled(true);
                button.usePauseText();
                break;

            case STOPPED:
                button.setEnabled(false);
                button.usePauseText();
                break;

            case PAUSED:
                button.useResumeText();
                break;

            case RESUMED:
                button.usePauseText();
                break;
        }
    }

    /**
     * handles mouse clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        emulator.togglePause();
    }
}