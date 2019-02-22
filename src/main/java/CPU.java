/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

public class CPU {

    private final Memory memory;
    private final DataRegisters dataRegisters;
    private final AddressRegister addressRegister;
    private final CallStack callStack;
    private final ProgrammCounter programmCounter;
    private final DelayTimer delayTimer;
    private final SoundTimer soundTimer;
    private final Keyboard keyboard;
    private final Graphics graphics;

    public CPU(Memory memory, DataRegisters dataRegisters, AddressRegister addressRegister,
            CallStack callStack, ProgrammCounter programmCounter, DelayTimer delayTimer,
            SoundTimer soundTimer, Keyboard keyboard, Graphics graphics) {
        this.memory = memory;
        this.dataRegisters = dataRegisters;
        this.addressRegister = addressRegister;
        this.callStack = callStack;
        this.programmCounter = programmCounter;
        this.delayTimer = delayTimer;
        this.soundTimer = soundTimer;
        this.keyboard = keyboard;
        this.graphics = graphics;
    }

}