package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.emulator.event.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.event.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.event.RunnerState;
import de.patrickmetz.clear_8.gui.Gui;

final public class GuiListener implements EmulatorEventListener {

    private final Gui gui;

    public GuiListener(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void handleRunnerEvent(EmulatorEvent e) {
        if (e.getState() == RunnerState.STOPPED) {
            gui.resetDisplay();
        }
    }

}
