/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 02:35.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

final public class Emulator {

    private final String FONT_PATH = "src/main/resources/font.bytecode";
    private final int FRAMES_PER_SECOND = 60;
    private final int INSTRUCTIONS_PER_FRAME;
    private final int INSTRUCTIONS_PER_SECOND;
    private final short MEMORY_OFFSET_FONT = 0;
    private final short MEMORY_OFFSET_ROM = 512;
    private final int MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;

    private final CentralProcessingUnit cpu;

    public Emulator(int instructionsPerSecond, boolean legacyMode) {
        INSTRUCTIONS_PER_SECOND = instructionsPerSecond;
        INSTRUCTIONS_PER_FRAME = INSTRUCTIONS_PER_SECOND / FRAMES_PER_SECOND;

        cpu = CentralProcessingUnitFactory.makeCpu(legacyMode);
    }

    public void run(String romPath) throws InterruptedException, IOException {
        cpu.writeToMemory(loadByteFile(FONT_PATH), MEMORY_OFFSET_FONT);
        cpu.writeToMemory(loadByteFile(romPath), MEMORY_OFFSET_ROM);

        long now = System.currentTimeMillis();
        long endOfFrameTime;

        while (true) {
            endOfFrameTime = now + MILLISECONDS_PER_FRAME;

            for (int i = 0; i < INSTRUCTIONS_PER_FRAME; i++) {
                cpu.processNextOpcode();
            }

            cpu.decrementTimers();

            while (System.currentTimeMillis() < endOfFrameTime) {
                Thread.sleep(1);
            }

            now = System.currentTimeMillis();
            // display.renderFrame();
        }
    }

    private int[] loadByteFile(String filePath) throws IOException {
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
