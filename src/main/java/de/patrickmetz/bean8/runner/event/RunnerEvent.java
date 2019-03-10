/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 10.03.19 16:14.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.runner.event;

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
