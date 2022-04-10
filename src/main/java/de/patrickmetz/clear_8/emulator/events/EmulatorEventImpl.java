package de.patrickmetz.clear_8.emulator.events;

import java.util.EventObject;

final public class EmulatorEventImpl extends EventObject implements EmulatorEvent {

    private EmulatorState status;

    public EmulatorEventImpl(Object source, EmulatorState status) {
        super(source);
        this.status = status;
    }

    @Override
    public EmulatorState getState() {
        return status;
    }

}
