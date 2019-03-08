/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 11:11.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import de.patrickmetz.bean8.emulator.Display;
import de.patrickmetz.bean8.emulator.Emulator;

public class Runner {

    private Display display;
    private Emulator emulator;

    private int instructionsPerSecond;
    private boolean isPaused;
    private boolean isRunning;
    private boolean legacyMode;
    private String romPath;

    Runner(String romPath, int instructionsPerSecond, boolean legacyMode) {
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

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isRunning() {
        return isRunning;
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
    }

    public void togglePause() {
        if (!isRunning) {
            return;
        }

        emulator.togglePause();
        isPaused = !isPaused;
    }
}
