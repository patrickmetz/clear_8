/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 24.02.19 12:14.
 * Copyright (c) 2019. All rights reserved.
 */

import emulator.*;

public class Emulator {

    private final int FRAMES_PER_SECOND = 60;
    private final int MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;
    private final CPU cpu;
    private final int instructionsPerSecond;

    public Emulator(int instructionsPerSecond) {
        this.instructionsPerSecond = instructionsPerSecond;

        cpu = new CPU(new AddressRegister(), new CallStack(), new DataRegisters(),
                new DelayTimer(), new Graphics(), new Keyboard(), new Memory(),
                new ProgrammCounter(), new SoundTimer(new Sound()));
    }

    public void run(String romPath) throws InterruptedException {
        final int INSTRUCTIONS_PER_FRAME = instructionsPerSecond / FRAMES_PER_SECOND;

        cpu.writeToMemory(512, loadRom(romPath));

        long now = System.currentTimeMillis();
        long endOfFrameTime;

        while (true) {
            endOfFrameTime = now + MILLISECONDS_PER_FRAME;

            for (int i = 0; i < INSTRUCTIONS_PER_FRAME; i++) {
                // cpu.processInstruction();
            }

            while (System.currentTimeMillis() < endOfFrameTime) {
                Thread.sleep(1);
            }

            now = System.currentTimeMillis();
            // display.renderFrame();
        }
    }

    private byte[] loadRom(String romPath) {
        throw new UnsupportedOperationException();
    }
}
