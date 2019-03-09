/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 14:24.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.runner.event;

import java.util.EventListener;

public interface RunnerEventListener extends EventListener {

    void handleRunnerEvent(RunnerEvent event);

}
