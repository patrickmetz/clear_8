package de.patrickmetz.clear_8.runner.event;

import java.util.EventObject;

final public class RunnerEvent extends EventObject {

    private RunnerState status;

    RunnerEvent(Object source, RunnerState status) {
        super(source);
        this.status = status;
    }

    public RunnerState getState() {
        return status;
    }

}
