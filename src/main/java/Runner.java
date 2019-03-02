/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 02.03.19 19:34.
 * Copyright (c) 2019. All rights reserved.
 */

import emulator.Emulator;

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

    void run() {
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
