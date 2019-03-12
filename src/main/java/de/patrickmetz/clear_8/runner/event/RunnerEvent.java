/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 13:59.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.runner.event;

import java.util.EventObject;

public class RunnerEvent extends EventObject {

    private RunnerState status;

    RunnerEvent(Object source, RunnerState status) {
        super(source);
        this.status = status;
    }

    public RunnerState getState() {
        return status;
    }

}
