package de.patrickmetz.clear_8.gui.timers;

import de.patrickmetz.clear_8.emulator.events.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.events.EmulatorState;
import de.patrickmetz.clear_8.globals.Config;
import de.patrickmetz.clear_8.gui.output.DisplayImpl;
import de.patrickmetz.clear_8.gui.output.StatusPane;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class FpsTimer implements EmulatorEventListener {
    private final StatusPane statusPane;
    private final Timer timer;

    private DisplayImpl display;

    public FpsTimer(DisplayImpl display, StatusPane statusPane) {
        this.display = display;
        this.statusPane = statusPane;

        timer = new Timer(
                Config.Gui.FPS_UPDATE_INTERVAL_IN_MS,
                new FpsTimerListener()
        );
    }

    @Override
    public void handleEmulatorEvent(EmulatorEvent e) {
        EmulatorState status = e.getState();

        if (status == EmulatorState.STARTED) {
            timer.start();
        } else if (status == EmulatorState.STOPPED) {
            timer.stop();
        }
    }

    public void setDisplay(DisplayImpl display) {
        this.display = display;

    }

    final private class FpsTimerListener implements ActionListener {

        private int updates;

        @Override
        public void actionPerformed(ActionEvent e) {
            int newUpdates = display.getUpdateCount();
            statusPane.updateFps("" + (newUpdates - updates));
            updates = newUpdates;
        }

    }

}
