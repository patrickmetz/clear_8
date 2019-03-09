/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 17:09.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.runner.event;

import javax.swing.event.EventListenerList;

public abstract class AbstractRunnerEventManager {

    private EventListenerList listeners = new EventListenerList();

    public void addListener(RunnerEventListener listener) {
        listeners.add(RunnerEventListener.class, listener);
    }

    public void removeListener(RunnerEventListener listener) {
        listeners.remove(RunnerEventListener.class, listener);
    }

    protected synchronized void fireEvent(RunnerStatus runnerStatus) {
        RunnerEvent runnerEvent = new RunnerEvent(this, runnerStatus);

        RunnerEventListener[] listenerList =
                listeners.getListeners(RunnerEventListener.class);

        for (RunnerEventListener listener : listenerList) {
            listener.handleRunnerEvent(runnerEvent);
        }

    }

}
