package de.patrickmetz.clear_8.emulator.event;

import java.util.EventObject;

final public class EmulatorEventImpl extends EventObject implements EmulatorEvent {

    private RunnerState status;

    public EmulatorEventImpl(Object source, RunnerState status) {
        super(source);
        this.status = status;
    }

    @Override
    public RunnerState getState() {
        return status;
    }

}
