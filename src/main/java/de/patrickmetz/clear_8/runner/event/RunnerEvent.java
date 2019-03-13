/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

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
