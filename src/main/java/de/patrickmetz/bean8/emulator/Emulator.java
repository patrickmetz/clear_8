/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 17:17.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator;

import de.patrickmetz.bean8.emulator.hardware.CentralProcessingUnit;
import de.patrickmetz.bean8.emulator.hardware.CentralProcessingUnitFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * The emulator runs on a separate thread, as a so called SwingWorker, in order to keep the GUI
 * responsive while updating the screen
 * <p>
 * see https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
 */
final public class Emulator extends SwingWorker<Void, boolean[][]> {

    private final int FRAMES_PER_SECOND = 60;
    private final int INSTRUCTIONS_PER_FRAME;
    private final short MEMORY_OFFSET_FONT = 0;
    private final short MEMORY_OFFSET_ROM = 512;
    private final int MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;

    private final CentralProcessingUnit cpu;
    private final Display screen;
    private boolean isPaused;
    private String romPath;

    public Emulator(String romPath, int instructionsPerSecond, boolean useVipCpu, Display screen) {
        this.romPath = romPath;
        this.screen = screen;

        INSTRUCTIONS_PER_FRAME = instructionsPerSecond / FRAMES_PER_SECOND;

        cpu = CentralProcessingUnitFactory.makeCpu(useVipCpu);
    }

    @Override
    public Void doInBackground() throws InterruptedException, IOException {
        cpu.writeToMemory(Font.getBytes(), MEMORY_OFFSET_FONT);
        cpu.writeToMemory(loadFileAsBytes(romPath), MEMORY_OFFSET_ROM);
        cpu.setProgramCounter(MEMORY_OFFSET_ROM);

        long now = System.currentTimeMillis();
        long endOfFrameTime;

        while (!isCancelled()) {

            // the emulator runs as a separate thread so we use thread safe pausing
            // see: https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html
            synchronized (this) {
                while (isPaused) {
                    wait();
                }
            }

            endOfFrameTime = now + MILLISECONDS_PER_FRAME;

            for (int i = 0; i < INSTRUCTIONS_PER_FRAME; i++) {
                cpu.processNextOpcode();
            }

            cpu.decrementTimers();

            while (System.currentTimeMillis() < endOfFrameTime) {
                Thread.sleep(1);
            }

            now = System.currentTimeMillis();
            publish(cpu.getScreenData());
        }

        return null;
    }

    /**
     * Reverses the current pause state in a synchronized (thread safe) way, and wakes the thread up
     * from a possible waiting phase.
     */
    public synchronized void togglePause() {
        isPaused = !isPaused;

        notify();
    }

    @Override
    protected void process(List<boolean[][]> screenData) {
        for (boolean[][] data : screenData) {
            screen.update(data);
        }
    }

    private int[] loadFileAsBytes(String filePath) throws IOException {
        File file = new File(filePath);
        int[] data = new int[(int) file.length()];
        FileInputStream fileStream = new FileInputStream(file);

        int byteCount = 0;
        int byteAsInteger;

        while ((byteAsInteger = fileStream.read()) != -1) {
            data[byteCount++] = byteAsInteger;
        }

        return data;
    }

}
