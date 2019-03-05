/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 05.03.19 11:14.
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

final public class Emulator extends SwingWorker<Void, boolean[][]> {

    private final int FRAMES_PER_SECOND = 60;
    private final int INSTRUCTIONS_PER_FRAME;
    private final short MEMORY_OFFSET_FONT = 0;
    private final short MEMORY_OFFSET_ROM = 512;
    private final int MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;

    private final CentralProcessingUnit cpu;
    private final iScreen screen;
    private String romPath;

    public Emulator(String romPath, int instructionsPerSecond, boolean legacyMode, iScreen screen) {
        this.romPath = romPath;
        this.screen = screen;

        INSTRUCTIONS_PER_FRAME = instructionsPerSecond / FRAMES_PER_SECOND;

        cpu = CentralProcessingUnitFactory.makeCpu(legacyMode);
    }

    @Override
    public Void doInBackground() throws InterruptedException, IOException {
        cpu.writeToMemory(Font.getBytes(), MEMORY_OFFSET_FONT);
        cpu.writeToMemory(loadFileAsBytes(romPath), MEMORY_OFFSET_ROM);
        cpu.setProgramCounter(MEMORY_OFFSET_ROM);

        long now = System.currentTimeMillis();
        long endOfFrameTime;

        while (!isCancelled()) {
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
