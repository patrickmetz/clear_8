package de.patrickmetz.clear_8.emulator.event;

public interface EmulatorEvent {
    RunnerState getState();

     Object getSource();
}
