/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 15.03.19 00:51.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.emulator.hardware;

import de.patrickmetz.clear_8.gui.listener.KeyboardListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CentralProcessingUnitSuperChipTest {

    private AddressRegister addressRegister;
    private CallStack callStack;
    private CentralProcessingUnitSuperChip cpu;
    private DataRegisters dataRegisters;
    private DelayTimer delayTimer;
    private Graphics graphics;
    private KeyboardListener keyboard;
    private Memory memory;
    private ProgramCounter programCounter;
    private SoundTimer soundTimer;

    /**
     * @see CentralProcessingUnitSuperChip#opcode1NNN
     */
    @Test
    void opcode1NNN() {
        writeInstruction(0x1123);
        cpu.processNextInstruction();

        assertEquals(0x0123, programCounter.read());
    }

    /**
     * @see CentralProcessingUnitSuperChip#opcode2NNN
     */
    @Test
    void opcode2NNN() {
        writeInstruction(0x2123);
        cpu.processNextInstruction();

        assertEquals(2, callStack.pop());
        assertEquals(0x0123, programCounter.read());
    }

    /**
     * @see CentralProcessingUnitSuperChip#opcode3XNN
     */
    @Test
    void opcode3NNN() {
        dataRegisters.write(1, 0x0023);

        writeInstruction(0x3123);
        cpu.processNextInstruction();

        assertEquals(4, programCounter.read());
    }

    /**
     * @see CentralProcessingUnitSuperChip#opcode4XNN
     */
    @Test
    void opcode4NNN() {
        dataRegisters.write(1, 0x0023);

        writeInstruction(0x4155);
        cpu.processNextInstruction();

        assertEquals(4, programCounter.read());
    }

    @BeforeEach
    void setUp() {
        addressRegister = new AddressRegister();
        callStack = new CallStack();
        dataRegisters = new DataRegisters();
        delayTimer = new DelayTimer();
        graphics = new Graphics();
        keyboard = new KeyboardListener();
        memory = new Memory();
        programCounter = new ProgramCounter();
        soundTimer = new SoundTimer(new Sound());

        cpu = new CentralProcessingUnitSuperChip(
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