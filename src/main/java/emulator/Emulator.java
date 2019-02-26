/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 26.02.19 17:42.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

final public class Emulator {

    private final int FRAMES_PER_SECOND = 60;
    private final int MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
    private final CentralProcessingUnit cpu;
    private final int instructionsPerSecond;

    public Emulator(int instructionsPerSecond, boolean legacyMode) {
        this.instructionsPerSecond = instructionsPerSecond;

        //todo: this looks quite ugly
        // https://stackoverflow.com/questions/1268817/create-new-class-from-a-variable-in-java
        if (legacyMode) {
            cpu = new CentralProcessingUnitLegacy(
                    new AddressRegister(), new CallStack(), new DataRegisters(),
                    new DelayTimer(), new Graphics(), new Keyboard(), new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound()));
        } else {
            cpu = new CentralProcessingUnit(
                    new AddressRegister(), new CallStack(), new DataRegisters(),
                    new DelayTimer(), new Graphics(), new Keyboard(), new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound()));
        }
    }

    public void run(String romPath) throws InterruptedException, IOException {
        final int INSTRUCTIONS_PER_FRAME = instructionsPerSecond / FRAMES_PER_SECOND;

        cpu.loadRomIntoMemory(loadRom(romPath));

        long now = System.currentTimeMillis();
        long endOfFrameTime;

        while (true) {
            endOfFrameTime = now + MILLISECONDS_PER_FRAME;

            for (int i = 0; i < INSTRUCTIONS_PER_FRAME; i++) {
                cpu.executeNextInstruction();
            }

            while (System.currentTimeMillis() < endOfFrameTime) {
                Thread.sleep(1);
            }

            now = System.currentTimeMillis();
            // display.renderFrame();
        }
    }

    private byte[] loadRom(String romPath) throws IOException {
        File file = new File(romPath);
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fileStream = new FileInputStream(file);

        int byteCount = 0;
        int byteAsInteger;

        while ((byteAsInteger = fileStream.read()) != -1) {
            bytes[byteCount++] = (byte) byteAsInteger;
        }

        return bytes;
    }
}
