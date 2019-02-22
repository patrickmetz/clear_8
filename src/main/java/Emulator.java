/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 21.02.19 21:56.
 * Copyright (c) 2019. All rights reserved.
 */

class Emulator {

    private final int FRAMES_PER_SECOND = 60;
    private final int MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;

    private final int instructionsPerSecond;

    private final AddressRegister addressRegister;
    private final CallStack callStack;
    private final CPU cpu;
    private final DataRegisters dataRegisters;
    private final DelayTimer delayTimer;
    private final Graphics graphics;
    private final Keyboard keyboard;
    private final Memory memory;
    private final ProgrammCounter programmCounter;
    private final Sound sound;
    private final SoundTimer soundTimer;

    Emulator(int instructionsPerSecond) {
        this.instructionsPerSecond = instructionsPerSecond;

        addressRegister = new AddressRegister();
        callStack = new CallStack();
        dataRegisters = new DataRegisters();
        delayTimer = new DelayTimer();
        graphics = new Graphics();
        keyboard = new Keyboard();
        memory = new Memory();
        programmCounter = new ProgrammCounter();
        sound = new Sound();
        soundTimer = new SoundTimer(sound);

        cpu = new CPU(addressRegister, callStack, dataRegisters, delayTimer, graphics, keyboard,
                memory, programmCounter, soundTimer);
    }

    void run() throws InterruptedException {
        final int INSTRUCTIONS_PER_FRAME = instructionsPerSecond / FRAMES_PER_SECOND;

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
}
