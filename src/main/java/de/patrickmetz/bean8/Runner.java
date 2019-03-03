/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 23:16.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import de.patrickmetz.bean8.emulator.Emulator;
import de.patrickmetz.bean8.emulator.Screen;

public class Runner {

    private int instructionsPerSecond;
    private boolean legacyMode;
    private String romPath;
    private Screen screen;

    Runner(String romPath, int instructionsPerSecond, boolean legacyMode) {
        this.romPath = romPath;
        this.instructionsPerSecond = instructionsPerSecond;
        this.legacyMode = legacyMode;
    }

    public void run() {

        Emulator emulator = new Emulator(
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

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
