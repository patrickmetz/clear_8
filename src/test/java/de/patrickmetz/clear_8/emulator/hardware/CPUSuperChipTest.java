/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 15.03.19 00:51.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.emulator.hardware;

import de.patrickmetz.clear_8.emulator.input.KeyboardImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CentralProcessingUnitSuperChipTest {

    private AddressRegister addressRegister;
    private CallStack     callStack;
    private CPUSuperChip  cpu;
    private DataRegisters dataRegisters;
    private DelayTimer delayTimer;
    private Graphics     graphics;
    private KeyboardImpl keyboard;
    private Memory       memory;
    private ProgramCounter programCounter;
    private SoundTimer soundTimer;

    /**
     * @see CPUSuperChip#opcode1NNN
     */
    @Disabled
    void opcode1NNN() {
        writeInstruction(0x1123);
        cpu.process();

        assertEquals(0x0123, programCounter.read());
    }

    /**
     * @see CPUSuperChip#opcode2NNN
     */
    @Disabled
    void opcode2NNN() {
        writeInstruction(0x2123);
        cpu.process();

        assertEquals(2, callStack.pop());
        assertEquals(0x0123, programCounter.read());
    }

    /**
     * @see CPUSuperChip#opcode3XNN
     */
    @Disabled
    void opcode3NNN() {
        dataRegisters.write(1, 0x0023);

        writeInstruction(0x3123);
        cpu.process();

        assertEquals(4, programCounter.read());
    }

    /**
     * @see CPUSuperChip#opcode4XNN
     */
    @Disabled
    void opcode4NNN() {
        dataRegisters.write(1, 0x0023);

        writeInstruction(0x4155);
        cpu.process();

        assertEquals(4, programCounter.read());
    }

    @BeforeEach
    void setUp() {
        addressRegister = new AddressRegister();
        callStack = new CallStack();
        dataRegisters = new DataRegisters();
        delayTimer = new DelayTimer();
        graphics = new Graphics();
        keyboard = new KeyboardImpl();
        memory = new Memory();
        programCounter = new ProgramCounter();
        soundTimer = new SoundTimer(new Sound());

        cpu = new CPUSuperChip(
                addressRegister, callStack, dataRegisters,
                delayTimer, graphics, keyboard, memory,
                programCounter, soundTimer
        );
    }

    private void writeInstruction(int i) {
        memory.write(0, (i & 0xFF00) >> 8);
        memory.write(1, i & 0x00FF);
    }

}