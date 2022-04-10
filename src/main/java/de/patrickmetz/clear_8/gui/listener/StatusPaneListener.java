package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.event.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.event.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.event.RunnerState;
import de.patrickmetz.clear_8.gui.component.output.StatusPane;

import java.io.File;

final public class StatusPaneListener implements EmulatorEventListener {

    private final StatusPane pane;

    public StatusPaneListener(StatusPane pane) {
        this.pane = pane;
    }

    @Override
    public void handleRunnerEvent(EmulatorEvent e) {
        RunnerState status = e.getState();

        if (status == RunnerState.STARTED) {
            Emulator emulator = (Emulator) e.getSource();

            pane.updateFileName(
                    new File(emulator.getGamePath()).getName()
            );
        } else if (status == RunnerState.STOPPED) {
            pane.updateFileName(null);
            pane.updateFps(null);
        }
    }

}
