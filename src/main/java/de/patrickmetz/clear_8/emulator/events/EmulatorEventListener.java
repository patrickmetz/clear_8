package de.patrickmetz.clear_8.emulator.events;

import java.util.EventListener;

public interface EmulatorEventListener extends EventListener {
    void handleEmulatorEvent(EmulatorEvent e);
}
