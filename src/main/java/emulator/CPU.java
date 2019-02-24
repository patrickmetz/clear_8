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

    void executeNextInstruction() {
        int instruction = getNextInstruction();
    }

    void loadRomIntoMemory(byte[] bytes) {
        short offset = Memory.OFFSET_ROM;

        for (byte b : bytes) {
            memory.write(offset++, b);
        }

        programCounter.write(Memory.OFFSET_ROM);
    }

    /**
     * Example:
     * <p>
     * first byte : 00000111
     * second byte: 01010010
     * <p>
     * instruction               : 00000000|00000000
     * instruction = first byte  : 00000000|00000111
     * instruction <<= 8         : 00000111|00000000
     * instruction |= second byte: 00000111|01010010
     */
    private int getNextInstruction() {
        int address = programCounter.read();

        int instruction = memory.read(address);
        instruction <<= 8;
        instruction |= memory.read(address + 1);

        programCounter.increment(2);

        return instruction;
    }
}