package de.patrickmetz.clear_8.emulator.events;

public interface EmulatorEvent {
    EmulatorState getState();

    Object getSource();
}
