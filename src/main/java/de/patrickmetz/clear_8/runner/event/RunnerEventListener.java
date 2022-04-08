package de.patrickmetz.clear_8.runner.event;

import java.util.EventListener;

public interface RunnerEventListener extends EventListener {

    void handleRunnerEvent(RunnerEvent e);

}
