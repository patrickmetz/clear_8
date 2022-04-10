package de.patrickmetz.clear_8.gui.timer;

import de.patrickmetz.clear_8.emulator.event.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.event.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.event.RunnerState;
import de.patrickmetz.clear_8.gui.component.output.DisplayImpl;
import de.patrickmetz.clear_8.gui.component.output.StatusPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final public class FpsTimer implements EmulatorEventListener {

    private static final int DELAY = 1000;

    private final StatusPane statusPane;
    private final Timer timer;

    private DisplayImpl display;

    public FpsTimer(DisplayImpl display, StatusPane statusPane) {
        this.display = display;
        this.statusPane = statusPane;

        timer = new Timer(
                DELAY,
                new FpsTimerListener()
        );
    }

    @Override
    public void handleRunnerEvent(EmulatorEvent e) {
        RunnerState status = e.getState();

        if (status == RunnerState.STARTED) {
            timer.start();
        } else if (status == RunnerState.STOPPED) {
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
