/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 13:18.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import de.patrickmetz.bean8.emulator.Emulator;

import java.io.IOException;

public class Runner {

    private int instructionsPerSecond;
    private boolean legacyMode;
    private String romPath;

    Runner(String romPath, int instructionsPerSecond, boolean legacyMode) {
        this.romPath = romPath;
        this.instructionsPerSecond = instructionsPerSecond;
        this.legacyMode = legacyMode;
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

    public void run() {
        Emulator emulator = new Emulator(
                instructionsPerSecond,
                legacyMode
        );

        try {
            emulator.run(romPath);
        } catch (IOException e) {
            //todo: log this
            e.printStackTrace();
        } catch (InterruptedException e) {
            //todo: log this
            e.printStackTrace();
        }
    }
}
