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

    protected static int unsignedByte(int value) {
        return value & 0xFF;
    }

    /**
     * @see CPUSuperChipImpl#opcode00EE
     */
    @Test
    void opcode00EE() {
        int value = 0x1234;
        callStack.push(value); // address before leaving subroutine
        writeOpcodeToMemory(0x00EE);
        processOpcode();
        assertEquals(value, programCounter.read()); // returned from subroutine?
    }

    /**
     * @see CPUSuperChipImpl#opcode1NNN
     */
    @Test
    void opcode1NNN() {
        writeOpcodeToMemory(0x1123); // NNN = 0x123
        processOpcode();

        assertEquals(0x123, programCounter.read()); // p.c. is NNN?
    }

    /**
     * @see CPUSuperChipImpl#opcode2NNN
     */
    @Test
    void opcode2NNN() {
        writeOpcodeToMemory(0x2123); // first opcode, NNN = 0x0123
        programCounter.write(0x0000); // set p.c. to first opcode
        processOpcode();

        assertEquals(0x2, callStack.pop()); // second opcode on stack?
        assertEquals(0x123, programCounter.read()); // p.c. is NNN?
    }

    /**
     * @see CPUSuperChipImpl#opcode3XNN
     */
    @Test
    void opcode3XNN() {
        registers.write(1, 0x0099);

        writeOpcodeToMemory(0x3199); // first opcode, X = 0x1, NN = 0x99
        processOpcode();

        assertEquals(0x4, programCounter.read()); // p.c. is at 3rd opcode?
    }

    /**
     * @see CPUSuperChipImpl#opcode4XNN
     */
    @Test
    void opcode4XNN() {
        registers.write(1, 0x0099);

        writeOpcodeToMemory(0x4177); // first opcode, X = 0x1, NN = 0x77
        processOpcode();

        assertEquals(0x4, programCounter.read()); // p.c. is at 3rd opcode?
    }

    /**
     * @see CPUSuperChipImpl#opcode5XY0
     */
    @Test
    void opcode5XY0() {
        int value = 0x0099;

        registers.write(0x0, value);
        registers.write(0x1, value);

        writeOpcodeToMemory(0x5010); // first opcode, X = 0x0, Y = 0x1
        processOpcode();

        assertEquals(0x4, programCounter.read()); // p.c. is at 3rd opcode?
    }

    /**
     * @see CPUSuperChipImpl#opcode6XNN
     */
    @Test
    void opcode6XNN() {
        int value = 0x0099;

        writeOpcodeToMemory(0x6099); // X = 0x0, NN = 99
        processOpcode();

        assertEquals(value, registers.read(0x0));
    }

    /**
     * @see CPUSuperChipImpl#opcode7XNN
     */
    @Test
    void opcode7XNNHandlesMaxByteValue() {
        registers.write(0x0, 0x0);

        writeOpcodeToMemory(0x70FF); // X = 0x0, NN = 255
        processOpcode();

        assertEquals(0x00FF, registers.read(0x0)); // 255 ?
    }

    /**
     * @see CPUSuperChipImpl#opcode7XNN
     */
    @Test
    void opcode7XNNHandlesOverflow() {
        registers.write(0x0, 0x1);

        writeOpcodeToMemory(0x70FF); // X = 0x0, NN = 255
        processOpcode();

        assertEquals(0x0, registers.read(0x0)); // 1 + 255 = 0 ?
    }

    /**
     * @see CPUSuperChipImpl#opcode8XY0
     */
    @Test
    void opcode8XY0() {
        registers.write(0x0, 0x0);
        registers.write(0x1, 0x1);

        writeOpcodeToMemory(0x8010); // X = 0x0, Y = 0x1
        processOpcode();

        assertEquals(0x1, registers.read(0x0));
    }


    /**
     * @see CPUSuperChipImpl#opcode8XY1
     */
    @Test
    void opcode8XY1(){
        registers.write(0x0, 0xF0);
        registers.write(0x1, 0x0F);

        writeOpcodeToMemory(0x8011); // X = 0x0, Y = 0x1
        processOpcode();

        assertEquals(0xFF, registers.read(0x0)); // X = (X OR Y)?
    }

    /**
     * @see CPUSuperChipImpl#opcode8XY2
     */
    @Test
    void opcode8XY2(){
        registers.write(0x0, 0xF0);
        registers.write(0x1, 0x0F);

        writeOpcodeToMemory(0x8012); // X = 0x0, Y = 0x1
        processOpcode();

        assertEquals(0x00, registers.read(0x0)); // X = (X AND Y)?
    }

    /**
     * @see CPUSuperChipImpl#opcode8XY3
     */
    @Test
    void opcode8XY3(){
        registers.write(0x0, 0xF0);
        registers.write(0x1, 0xFF);

        writeOpcodeToMemory(0x8013); // X = 0x0, Y = 0x1
        processOpcode();

        assertEquals(0x0F, registers.read(0x0)); // X = (X XOR Y)?
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