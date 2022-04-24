/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 15.03.19 00:51.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.emulator.hardware;

import de.patrickmetz.clear_8.emulator.input.KeyboardImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CentralProcessingUnitSuperChipTest {

    private AddressRegister  addressRegister;
    private CallStack        callStack;
    private CPUSuperChipImpl cpu;
    private Registers        registers;
    private DelayTimer       delayTimer;
    private Graphics         graphics;
    private KeyboardImpl     keyboard;
    private Memory           memory;
    private ProgramCounter   programCounter;
    private SoundTimer       soundTimer;

    /**
     * @see CPUSuperChipImpl#opcode1NNN
     */
    @Test
    void opcode1NNN() {
        writeBytePairToMemory(0, 0x1123); // NNN = 0x0123
        cpu.process(1); // set p.c. to NNN

        assertEquals(0x0123, programCounter.read()); // p.c. is NNN?
    }

    /**
     * @see CPUSuperChipImpl#opcode2NNN
     */
    @Test
    void opcode2NNN() {
        writeBytePairToMemory(0, 0x2123); // first opcode, NNN = 0x0123
        programCounter.write(0x0000); // set p.c. to first opcode
        cpu.process(1); // move p.c. by one opcode, push new address, set p.c. to NNN

        assertEquals(0x0002, callStack.pop()); // second opcode on stack?
        assertEquals(0x0123, programCounter.read()); // p.c. is NNN?
    }

    /**
     * @see CPUSuperChipImpl#opcode3XNN
     */
    @Test
    void opcode3XNN() {
        registers.write(1, 0x0099); // set 1st register to 0x99

        writeBytePairToMemory(0, 0x3199); // first opcode, X = 0x1, NN = 0x99
        cpu.process(1); // move p.c. by one opcode, skip next opcode if 1st register is 0x99

        assertEquals(0x4, programCounter.read()); // p.c. is at 3rd opcode?
    }

    /**
     * @see CPUSuperChipImpl#opcode4XNN
     */
    @Test
    void opcode4XNN() {
        registers.write(1, 0x0099); // set 1st register to 0x99

        writeBytePairToMemory(0, 0x4177); // first opcode, X = 0x1, NN = 0x77
        cpu.process(1); // move p.c. by one opcode, skip next opcode if 1st register isn't 0x77

        assertEquals(0x4, programCounter.read()); // p.c. is at 3rd opcode?
    }

    @BeforeEach
    void setUp() {
        addressRegister = new AddressRegister();
        callStack = new CallStack();
        registers = new Registers();
        delayTimer = new DelayTimer();
        graphics = new Graphics();
        keyboard = new KeyboardImpl();
        memory = new Memory();
        programCounter = new ProgramCounter();
        soundTimer = new SoundTimer(new Sound());

        cpu = new CPUSuperChipImpl(
                addressRegister, callStack, registers,
                delayTimer, graphics, keyboard, memory,
                programCounter, soundTimer
        );
    }

    private void writeBytePairToMemory(int address, int bits) {
        memory.write(address, (bits & 0xFF00) >> 8); // 1st eight bits
        memory.write(address + 1, bits & 0x00FF); // 2nd eight bits
    }

}