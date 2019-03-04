/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 04.03.19 20:00.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator;

import de.patrickmetz.bean8.emulator.hardware.CentralProcessingUnit;
import de.patrickmetz.bean8.emulator.hardware.CentralProcessingUnitFactory;

import javax.swing.*;
import java.io.*;
import java.util.List;

final public class Emulator extends SwingWorker<Void, boolean[][]> {

    private final int FRAMES_PER_SECOND = 60;
    private final int INSTRUCTIONS_PER_FRAME;
    private final int INSTRUCTIONS_PER_SECOND;
    private final short MEMORY_OFFSET_FONT = 0;
    private final short MEMORY_OFFSET_ROM = 512;
    private final int MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;

    private final CentralProcessingUnit cpu;
    private final Screen screen;
    private String romPath;

    public Emulator(String romPath, int instructionsPerSecond, boolean legacyMode, Screen screen) {
        this.romPath = romPath;

        INSTRUCTIONS_PER_SECOND = instructionsPerSecond;
        INSTRUCTIONS_PER_FRAME = INSTRUCTIONS_PER_SECOND / FRAMES_PER_SECOND;

        this.screen = screen;
        cpu = CentralProcessingUnitFactory.makeCpu(legacyMode);
    }

    public Void doInBackground() {
        cpu.writeToMemory(Font.getBytes(), MEMORY_OFFSET_FONT);
        cpu.writeToMemory(loadByteFile(romPath), MEMORY_OFFSET_ROM);

        long now = System.currentTimeMillis();
        long endOfFrameTime;

        while (!isCancelled()) {
            endOfFrameTime = now + MILLISECONDS_PER_FRAME;

            for (int i = 0; i < INSTRUCTIONS_PER_FRAME; i++) {
                cpu.processNextOpcode();
            }

            cpu.decrementTimers();

            while (System.currentTimeMillis() < endOfFrameTime) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            now = System.currentTimeMillis();
            publish(cpu.getScreenData());
        }

        return null;
    }

    @Override
    protected void process(List<boolean[][]> data) {
        for (int i = 0; i < data.size(); i++) {
            screen.update(data.get(i));
        }
    }

    private int[] loadByteFile(String filePath) {
        InputStream in = getClass().getResourceAsStream(filePath);

        File file = new File(filePath);
        int[] data = new int[(int) file.length()];
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int byteCount = 0;
        int byteAsInteger = 0;

        while (true) {
            try {
                if (!((byteAsInteger = fileStream.read()) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            data[byteCount++] = byteAsInteger;
        }

        return data;
    }
}
