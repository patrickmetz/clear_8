/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.runner;

import de.patrickmetz.clear_8.emulator.Display;
import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.Keyboard;
import de.patrickmetz.clear_8.runner.event.AbstractRunnerEventManager;
import de.patrickmetz.clear_8.runner.event.RunnerState;

final public class Runner extends AbstractRunnerEventManager {

    // input and output
    private Display display;
    private Keyboard keyboard;

    // emulator and its arguments
    private Emulator emulator;
    private int instructionsPerSecond;
    private String romPath;
    private boolean useVipCpu;

    // internal state of the runner
    private boolean isPaused;
    private boolean isRunning;

    public Runner(String romPath, int instructionsPerSecond, boolean useVipCpu) {
        this.romPath = romPath;
        this.instructionsPerSecond = instructionsPerSecond;
        this.useVipCpu = useVipCpu;
    }

    public int getInstructionsPerSecond() {
        return instructionsPerSecond;
    }

    public void setInstructionsPerSecond(int instructionsPerSecond) {
        this.instructionsPerSecond = instructionsPerSecond;
    }

    public String getRomPath() {
        return romPath;
    }

    public void setRomPath(String romPath) {
        this.romPath = romPath;
    }

    public boolean getUseVipCpu() {
        return useVipCpu;
    }

    public void setUseVipCpu(boolean useVipCpu) {
        this.useVipCpu = useVipCpu;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void start() {
        if (isRunning) {
            return;
        }

        isRunning = true;

        emulator = new Emulator(
                romPath,
                instructionsPerSecond,
                useVipCpu,
                display,
                keyboard
        );

        emulator.execute();

        fireEvent(RunnerState.STARTED);
    }

    public void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;
        isPaused = false;

        emulator.cancel(true);

        fireEvent(RunnerState.STOPPED);
    }

    public void togglePause() {
        if (!isRunning) {
            return;
        }

        isPaused = !isPaused;

        emulator.togglePause();

        if (isPaused) {
            fireEvent(RunnerState.PAUSED);
        } else {
            fireEvent(RunnerState.RESUMED);
        }
    }

}
