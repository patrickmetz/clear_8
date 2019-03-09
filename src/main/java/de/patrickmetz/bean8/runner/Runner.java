/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 17:21.
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
    private String romPath;
    private boolean useVipCpu;

    public Runner(String romPath, int instructionsPerSecond, boolean useVipCpu) {
        this.romPath = romPath == null ? "" : romPath;
        this.instructionsPerSecond = instructionsPerSecond;
        this.useVipCpu = useVipCpu;
    }

    public String getRomPath() {
        return romPath;
    }

    public boolean getUseVipCpu() {
        return useVipCpu;
    }

    public void run() {
        if (isRunning) {
            return;
        }

        isRunning = true;

        emulator = new Emulator(
                romPath,
                instructionsPerSecond,
                useVipCpu,
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

    public void setRomPath(String romPath) {
        this.romPath = romPath;
    }

    public void setUseVipCpu(boolean useVipCpu) {
        this.useVipCpu = useVipCpu;
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
