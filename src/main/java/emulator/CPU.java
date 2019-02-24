/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 23.02.19 19:33.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class CPU {

    private final AddressRegister addressRegister;
    private final CallStack callStack;
    private final DataRegisters dataRegisters;
    private final DelayTimer delayTimer;
    private final Graphics graphics;
    private final Keyboard keyboard;
    private final Memory memory;
    private final ProgramCounter programCounter;
    private final SoundTimer soundTimer;

    CPU(AddressRegister addressRegister, CallStack callStack, DataRegisters dataRegisters,
            DelayTimer delayTimer, Graphics graphics, Keyboard keyboard, Memory memory,
            ProgramCounter programCounter,
            SoundTimer soundTimer) {

        this.addressRegister = addressRegister;
        this.callStack = callStack;
        this.dataRegisters = dataRegisters;
        this.delayTimer = delayTimer;
        this.graphics = graphics;
        this.keyboard = keyboard;
        this.memory = memory;
        this.programCounter = programCounter;
        this.soundTimer = soundTimer;
    }

    void loadRomIntoMemory(byte[] bytes) {
        short offset = Memory.OFFSET_ROM;

        for (byte b : bytes) {
            memory.write((short) offset++, b);
        }

        programCounter.set(Memory.OFFSET_ROM);
    }
}