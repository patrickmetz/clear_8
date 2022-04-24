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

class CPUSuperChipImplTest {

    private AddressRegister addressRegister;
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
     * @see CPUSuperChipImpl#opcode00EE
     */
    @Test
    void opcode00EE() {
        callStack.push(0x1234); // address before leaving subroutine
        writeOpcodeToMemory(0x00EE);
        processOpcode();
        assertEquals(0x1234, programCounter.read()); // returned from subroutine?
    }

    /**
     * @see CPUSuperChipImpl#opcode1NNN
     */
    @Test
    void opcode1NNN() {
        writeOpcodeToMemory(0x1123); // NNN = 0x0123
        processOpcode();

        assertEquals(0x0123, programCounter.read()); // p.c. is NNN?
    }

    /**
     * @see CPUSuperChipImpl#opcode2NNN
     */
    @Test
    void opcode2NNN() {
        writeOpcodeToMemory(0x2123); // first opcode, NNN = 0x0123
        programCounter.write(0x0000); // set p.c. to first opcode
        processOpcode();

        assertEquals(0x0002, callStack.pop()); // second opcode on stack?
        assertEquals(0x0123, programCounter.read()); // p.c. is NNN?
    }

    /**
     * @see CPUSuperChipImpl#opcode3XNN
     */
    @Test
    void opcode3XNN() {
        registers.write(1, 0x0099); // set 1st register to 0x99

        writeOpcodeToMemory(0x3199); // first opcode, X = 0x1, NN = 0x99
        processOpcode();

        assertEquals(0x4, programCounter.read()); // p.c. is at 3rd opcode?
    }

    /**
     * @see CPUSuperChipImpl#opcode4XNN
     */
    @Test
    void opcode4XNN() {
        registers.write(1, 0x0099); // set 1st register to 0x99

        writeOpcodeToMemory(0x4177); // first opcode, X = 0x1, NN = 0x77
        processOpcode();

        assertEquals(0x4, programCounter.read()); // p.c. is at 3rd opcode?
    }

    /**
     * @see CPUSuperChipImpl#opcode5XY0
     */
    @Test
    void opcode5XY0(){
        int value = 0x0099;

        registers.write(0x0, value);  // set two registers to same value
        registers.write(0x1, value);

        writeOpcodeToMemory(0x5010); // first opcode, X = 0x0, Y = 0x1
        processOpcode();

        assertEquals(0x4, programCounter.read()); // p.c. is at 3rd opcode?
    }
    /**
     * @see CPUSuperChipImpl#opcode6XNN
     */
    @Test
    void opcode6XNN(){
        int value = 0x0099;

        writeOpcodeToMemory(0x6099); // first opcode, X = 0x0, NN = 99
        processOpcode();

        assertEquals(value, registers.read(0x0)); // 1st register is 0x99?
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

    private void processOpcode() {
        cpu.process(1);
    }

    private void writeOpcodeToMemory(int bits) {
        memory.write(0, (bits & 0xFF00) >> 8); // 1st eight bits
        memory.write(1, bits & 0x00FF); // 2nd eight bits
    }

}