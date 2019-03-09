/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 14:30.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.runner;

import de.patrickmetz.bean8.emulator.Display;
import de.patrickmetz.bean8.emulator.Emulator;
import de.patrickmetz.bean8.runner.event.AbstractRunnerEventManager;
import de.patrickmetz.bean8.runner.event.RunnerStatus;

public class Runner extends AbstractRunnerEventManager {

    private Display display;
    private Emulator emulator;

    private int instructionsPerSecond;
    private boolean isPaused;
    private boolean isRunning;
    private boolean legacyMode;
    private String romPath;

    public Runner(String romPath, int instructionsPerSecond, boolean legacyMode) {
        this.romPath = romPath == null ? "" : romPath;
        this.instructionsPerSecond = instructionsPerSecond;
        this.legacyMode = legacyMode;
    }

    public boolean getLegacyMode() {
        return legacyMode;
    }

    public String getRomPath() {
        return romPath;
    }

    public void run() {
        if (isRunning) {
            return;
        }

        isRunning = true;

        emulator = new Emulator(
                romPath,
                instructionsPerSecond,
                legacyMode,
                display
        );

        emulator.execute();

        fireEvent(RunnerStatus.STARTED);
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public void setInstructionsPerSecond(int instructionsPerSecond) {
        this.instructionsPerSecond = instructionsPerSecond;
    }

    public void setLegacyMode(boolean legacyMode) {
        this.legacyMode = legacyMode;
    }

    public void setRomPath(String romPath) {
        this.romPath = romPath;
    }

    public void stop() {
        if (!isRunning) {
            return;
        }

        emulator.cancel(true);
        isRunning = false;

        fireEvent(RunnerStatus.STOPPED);
    }

    public void togglePause() {
        if (!isRunning) {
            return;
        }

        emulator.togglePause();
        isPaused = !isPaused;

        if (isPaused) {
            fireEvent(RunnerStatus.PAUSED);
        } else {
            fireEvent(RunnerStatus.RESUMED);
        }
    }

}
