package de.patrickmetz.clear_8.gui.listeners;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.events.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.events.EmulatorState;
import de.patrickmetz.clear_8.gui.output.StatusPane;

import java.io.File;

final public class StatusPaneListener implements EmulatorEventListener {

    private final StatusPane pane;

    public StatusPaneListener(StatusPane pane) {
        this.pane = pane;
    }

    @Override
    public void handleEmulatorEvent(EmulatorEvent e) {
        EmulatorState status = e.getState();

        if (status == EmulatorState.STARTED) {
            Emulator emulator = (Emulator) e.getSource();

            pane.updateFileName(
                    new File(emulator.getGamePath()).getName()
            );
        } else if (status == EmulatorState.STOPPED) {
            pane.updateFileName(null);
            pane.updateFps(null);
        }
    }

}
