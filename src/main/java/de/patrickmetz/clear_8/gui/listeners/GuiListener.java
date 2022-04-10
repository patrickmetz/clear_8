package de.patrickmetz.clear_8.gui.listeners;

import de.patrickmetz.clear_8.emulator.events.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.events.EmulatorState;
import de.patrickmetz.clear_8.gui.Gui;

final public class GuiListener implements EmulatorEventListener {

    private final Gui gui;

    public GuiListener(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void handleEmulatorEvent(EmulatorEvent e) {
        if (e.getState() == EmulatorState.STOPPED) {
            gui.resetDisplay();
        }
    }

}
