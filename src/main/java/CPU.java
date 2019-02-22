/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

public class CPU {

    private final AddressRegister addressRegister;
    private final CallStack callStack;
    private final DataRegisters dataRegisters;
    private final DelayTimer delayTimer;
    private final Graphics graphics;
    private final Keyboard keyboard;
    private final Memory memory;
    private final ProgrammCounter programmCounter;
    private final SoundTimer soundTimer;

    public CPU(AddressRegister addressRegister, CallStack callStack, DataRegisters dataRegisters,
            DelayTimer delayTimer, Graphics graphics, Keyboard keyboard, Memory memory,
            ProgrammCounter programmCounter,
            SoundTimer soundTimer) {

        this.addressRegister = addressRegister;
        this.callStack = callStack;
        this.dataRegisters = dataRegisters;
        this.delayTimer = delayTimer;
        this.graphics = graphics;
        this.keyboard = keyboard;
        this.memory = memory;
        this.programmCounter = programmCounter;
        this.soundTimer = soundTimer;
    }

}