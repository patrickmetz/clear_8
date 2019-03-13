/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:13.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.emulator;

import de.patrickmetz.clear_8.emulator.hardware.CentralProcessingUnit;
import de.patrickmetz.clear_8.emulator.hardware.CentralProcessingUnitFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * The emulator basically loops forever, or until it is shut down for good.
 * In order for the GUI to not completely freeze, while waiting for the
 * emulator to return something, the emulator needs to be put on a separate Thread.
 * <p><p>
 * But it needs to be a special thread, ensuring that we only update the
 * GUI when it's ready to be updated. So we extend SwingWorker and implement
 * or use the necessary methods:
 * <p><p>
 * doInBackground() is the method where a SwingWorker does its work
 * (the emulation loop in our case).
 * <p><p>
 * publish() sends intermediate work results from within doInBackground()
 * (display data in our case) to the GUI, where they are scheduled for
 * later processing.
 * <p><p>
 * isCancelled() is used from within doInBackground(), to check if the
 * emulator was shut down from the gui side.
 * <p><p>
 * process() is called by the GUI from the outside, whenever it is ready
 * to be updated with some of the already collected work results.
 * <p><p>
 * The weird extends statement (extends SwingWorker...)
 * is a Java Generic, which in this case means that the doInBackground()
 * method returns nothing (Void) when it ends, and that the publish() method
 * returns a two dimensional array of truth values (boolean[][]) which is
 * send to process().
 * <p><p>
 * see https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
 */
final public class Emulator extends SwingWorker<Void, boolean[][]> {

    private final int FRAMES_PER_SECOND = 60;
    private final int FRAME_DURATION = 1000 / FRAMES_PER_SECOND;
    private final int INSTRUCTIONS_PER_FRAME;

    private final int MEMORY_OFFSET_FONT = 0;
    private final int MEMORY_OFFSET_ROM = 512;

    private final CentralProcessingUnit cpu;
    private final Display display;

    private boolean isPaused;

    private String romPath;

    public Emulator(
            String romPath,
            int instructionsPerSecond,
            boolean useVipCpu,
            Display display,
            Keyboard keyboard
    ) {
        this.romPath = romPath;
        this.display = display;

        INSTRUCTIONS_PER_FRAME = instructionsPerSecond / FRAMES_PER_SECOND;

        cpu = CentralProcessingUnitFactory.makeCpu(useVipCpu, keyboard);
    }

    /**
     * the main loop / game loop
     */
    @Override
    public Void doInBackground() throws InterruptedException, IOException {
        cpu.writeToMemory(Font.getBytes(), MEMORY_OFFSET_FONT);
        cpu.writeToMemory(loadFileAsBytes(romPath), MEMORY_OFFSET_ROM);
        cpu.setProgramCounter(MEMORY_OFFSET_ROM);

        long now = System.currentTimeMillis();

        while (!isCancelled()) {
            waitUntilPauseEnds();

            for (int i = 0; i < INSTRUCTIONS_PER_FRAME; i++) {
                cpu.processNextInstruction();
            }

            cpu.updateTimers();

            waitUntilFrameEnds(now + FRAME_DURATION);
            now = System.currentTimeMillis();

            // sends screen data to the gui
            publish(cpu.getDisplayData());
        }

        return null;
    }

    /**
     * thread safe pause toggling
     * <p>
     * see: https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html
     */
    public synchronized void togglePause() {
        isPaused = !isPaused;

        notify();
    }

    /**
     * display data is send back here by the GUI,
     * when it's ready to be updated
     */
    @Override
    protected void process(List<boolean[][]> displayData) {
        for (boolean[][] data : displayData) {
            display.update(data);
        }
    }

    /**
     * loads a file as an array of bytes
     */
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

    /**
     * resource friendly waiting until a frame ends
     */
    private void waitUntilFrameEnds(long endOfFrameTime) throws InterruptedException {
        while (System.currentTimeMillis() < endOfFrameTime) {
            Thread.sleep(1);
        }
    }

    /**
     * thread safe waiting while paused
     * <p>
     * see: https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html
     */
    private synchronized void waitUntilPauseEnds() throws InterruptedException {
        while (isPaused) {
            wait();
        }
    }

}
