/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 14:35.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.runner.event;

import java.util.EventObject;

public class RunnerEvent extends EventObject {

    private RunnerStatus status;

    RunnerEvent(Object source, RunnerStatus status) {
        super(source);
        this.status = status;
    }

    public RunnerStatus getStatus() {
        return status;
    }

}
