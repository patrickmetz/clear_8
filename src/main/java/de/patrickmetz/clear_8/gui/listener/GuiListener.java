package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.gui.Gui;
import de.patrickmetz.clear_8.runner.event.RunnerEvent;
import de.patrickmetz.clear_8.runner.event.RunnerEventListener;
import de.patrickmetz.clear_8.runner.event.RunnerState;

final public class GuiListener implements RunnerEventListener {

    private final Gui gui;

    public GuiListener(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        if (e.getState() == RunnerState.STOPPED) {
            gui.resetDisplay();
        }
    }

}
