package de.patrickmetz.clear_8.emulator.event;

import java.util.EventListener;

public interface EmulatorEventListener extends EventListener {
    void handleRunnerEvent(EmulatorEvent e);
}
