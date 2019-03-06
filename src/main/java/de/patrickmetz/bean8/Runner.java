/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 06.03.19 15:56.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import de.patrickmetz.bean8.emulator.Emulator;
import de.patrickmetz.bean8.emulator.iScreen;

public class Runner {

    private Emulator emulator;
    private int instructionsPerSecond;
    private boolean isPaused;
    private boolean isRunning;
    private boolean legacyMode;
    private String romPath;
    private iScreen screen;

    Runner(String romPath, int instructionsPerSecond, boolean legacyMode) {
        this.romPath = romPath == null ? "" : romPath;
        this.instructionsPerSecond = instructionsPerSecond;
        this.legacyMode = legacyMode;
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
        isRunning = true;

        emulator = new Emulator(
                romPath,
                instructionsPerSecond,
                legacyMode,
                screen
        );

        emulator.execute();
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

    public void setScreen(iScreen screen) {
        this.screen = screen;
    }

    public void stop() {
        emulator.cancel(true);
        isRunning = false;
    }

    public void togglePause() {
        emulator.togglePause();
        isPaused = !isPaused;
    }
}
