/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 22:09.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

/**
 * CPU implementation using opcodes of the SCHIP (Super Chip)
 * <p>
 * see:
 * http://www.mattmik.com/files/chip8/mastering/chip8.html
 * http://devernay.free.fr/hacks/chip8/C8TECH10.HTM
 * https://github.com/Chromatophore/HP48-Superchip#behavior-and-quirk-investigations
 */
class CentralProcessingUnit {

    public static final int FONT_SIZE_IN_BYTES = 5;
    protected static final int CARRY_FLAG = 0xF;
    protected static final int EXPOSE_X = 0x0F00;
    protected static final int EXPOSE_Y = 0x00F0;
    protected static final int GET_N = 0x000F;
    protected static final int GET_NN = 0x00FF;
    protected static final int GET_NNN = 0x0FFF;
    protected static final int GET_UNSIGNED_BYTE = 0xFF;
    protected static final int GET_X = 8;
    protected static final int GET_Y = 4;
    protected static final int UNSIGNED_BYTE_MAX_VALUE = 255;
    protected final AddressRegister addressRegister;
    protected final DataRegisters dataRegisters;
    private final CallStack callStack;
    private final DelayTimer delayTimer;
    private final Graphics graphics;
    private final Keyboard keyboard;
    private final Memory memory;
    private final ProgramCounter programCounter;
    private final SoundTimer soundTimer;

    CentralProcessingUnit(AddressRegister addressRegister, CallStack callStack,
                          DataRegisters dataRegisters, DelayTimer delayTimer,
                          Graphics graphics, Keyboard keyboard, Memory memory,
                          ProgramCounter programCounter, SoundTimer soundTimer
    ) {
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

    private static int X(int i) {
        return (i & EXPOSE_X) >> GET_X;
    }

    private static int Y(int i) {
        return (i & EXPOSE_Y) >> GET_Y;
    }

    private static int unsigned(int value) {
        return value & 0xFF;
    }

    /**
     * modifies the value of data register X
     * by shifting it to the right by one bit
     * (equivalent to dividing by two).
     * <p>
     * the least significant bit of the
     * former value is stored in the carry register
     * <p>
     *
     * @see CentralProcessingUnitLegacy#execute8XY6(int)
     */
    protected void execute8XY6(int i) {
        int xAddress = X(i);
        int xValue = dataRegisters.read(xAddress);

        dataRegisters.write(CARRY_FLAG, xValue & 1);
        dataRegisters.write(xAddress, xValue >> 1);
    }

    /**
     * sets registers 0 to X to consecutive memory values
     * beginning at registered memory address
     *
     * @see CentralProcessingUnitLegacy#executeFX65(int)
     */
    protected void executeFX65(int i) {
        int endRegister = X(i);
        int memoryOffset = addressRegister.read();

        for (int j = 0; j <= endRegister; j++) {
            dataRegisters.write(
                    j,
                    memory.read(memoryOffset++)
            );
        }
    }

    void decrementTimers() {
        delayTimer.decrement();
        soundTimer.decrement();
    }

    /**
     * example:
     * <p>
     * instruction = 4645 as int
     * instruction = 1225 as hexadecimal
     * <p>
     * => 1NNN = jump to memory address NNN
     * see https://en.wikipedia.org/wiki/CHIP-8#Opcode_table
     * for all supported cpu instructions
     * <p>
     * 1225 & 0xF000 = 1000 (exposes 1st digit) | 1000 >> 12 = 1
     * 1225 & 0x0F00 = 0200 (exposes 2nd digit  | 0200 >> 8  = 2
     * 1225 & 0x00F0 = 0020 (exposes 3rd digit) | 0020 >> 4  = 2
     * 1225 & 0x000F = 0005 (exposes 4th digit) | 0005       = 5
     */
    void processNextInstruction() throws UnsupportedOperationException {
        int instruction = getNextInstruction();

        switch (instruction & 0xF000) {
            case 0x0000:
                switch (instruction & 0x00FF) {
                    case 0x00E0:
                        execute00E0(instruction);
                        break;
                    case 0x00EE:
                        execute00EE(instruction);
                        break;
                    default:
                        throwInstructionException(instruction);
                }
                break;
            case 0x1000:
                execute1NNN(instruction);
                break;
            case 0x2000:
                execute2NNN(instruction);
                break;
            case 0x3000:
                execute3XNN(instruction);
                break;
            case 0x4000:
                execute4XNN(instruction);
                break;
            case 0x5000:
                execute5XY0(instruction);
                break;
            case 0x6000:
                execute6XNN(instruction);
                break;
            case 0x7000:
                execute7XNN(instruction);
                break;
            case 0x8000:
                switch (instruction & 0x000F) {
                    case 0x0000:
                        execute8XY0(instruction);
                        break;
                    case 0x0004:
                        execute8XY4(instruction);
                        break;
                    case 0x0005:
                        execute8XY5(instruction);
                        break;
                    case 0x0006:
                        execute8XY6(instruction);
                        break;
                    default:
                        throwInstructionException(instruction);
                }
                break;
            case 0xA000:
                executeANNN(instruction);
                break;
            case 0xC000:
                executeCXNN(instruction);
                break;
            case 0xD000:
                executeDXYN(instruction);
                break;
            case 0xE000:
                switch (instruction & 0x000F) {
                    case 0x000E:
                        executeEX9E(instruction);
                        break;
                    case 0x0001:
                        executeEXA1(instruction);
                        break;
                    default:
                        throwInstructionException(instruction);
                }
                break;
            case 0xF000:
                switch (instruction & 0x00FF) {
                    case 0x001E:
                        executeFX1E(instruction);
                        break;
                    case 0x0007:
                        executeFX07(instruction);
                        break;
                    case 0x000A:
                        executeFX0A(instruction);
                        break;
                    case 0x0015:
                        executeFX15(instruction);
                        break;
                    case 0x0029:
                        executeFX29(instruction);
                        break;
                    case 0x0033:
                        executeFX33(instruction);
                        break;
                    case 0x0065:
                        executeFX65(instruction);
                        break;
                    default:
                        throwInstructionException(instruction);
                }
                break;
            default:
                throwInstructionException(instruction);
        }

    }

    void writeToMemory(int[] data, int offset) {
        for (int b : data) {
            memory.write(offset++, b);
        }
    }

    /**
     * clears the screen
     */
    private void execute00E0(int i) {
        graphics.clearScreen();
    }

    /**
     * leaving subroutine by popping former memory
     * location from the stack and jumping there
     */
    private void execute00EE(int i) {
        programCounter.write(callStack.pop());
    }

    /**
     * sets program counter to value NNN
     */
    private void execute1NNN(int i) {
        programCounter.write(i & GET_NNN);
    }

    /**
     * execute subroutine by pushing current memory
     * location to the stack and jumping to the
     * subroutine's location
     */
    private void execute2NNN(int i) {
        callStack.push(programCounter.read());
        programCounter.write(i & GET_NNN);
    }

    /**
     * skips one instruction if the value of
     * data register X is equal to NN
     */
    private void execute3XNN(int i) {
        if (
                dataRegisters.read(X(i))
                == (i & GET_NN)
        ) {
            // one instruction = 2 bytes
            programCounter.increment(2);
        }
    }

    /**
     * skip one instruction, if the value of data register X
     * is not equal to the value NN
     */
    private void execute4XNN(int i) {
        if (
                dataRegisters.read(X(i))
                != (i & GET_NN)
        ) {
            programCounter.increment(2);
        }
    }

    /**
     * skip one instruction, if the value of data register X
     * is equal to the value of data register Y
     */
    private void execute5XY0(int i) {
        if (
                dataRegisters.read(X(i))
                == dataRegisters.read(Y(i))

        ) {
            programCounter.increment(2);
        }
    }

    /**
     * sets data register X to value NN
     */
    private void execute6XNN(int i) {
        dataRegisters.write(
                X(i),
                i & GET_NN
        );
    }

    /**
     * Adds the value NN to data register X.
     * If the new value exceeds maximum unsigned byte size,
     * it is "wrapped around" with modulo, but no carry
     * flag is set.
     */
    private void execute7XNN(int i) {
        int addressX = X(i);

        int newValue = unsigned(dataRegisters.read(addressX))
                       + (i & GET_NN);

        //  > 255 ? wrap around at 255+1 -> e.g. 256 = 0, 257 = 1, etc...
        if (newValue > UNSIGNED_BYTE_MAX_VALUE) {
            newValue %= UNSIGNED_BYTE_MAX_VALUE + 1;
        }

        dataRegisters.write(addressX, newValue);
    }

    /**
     * sets the value of data register X
     * to the value of data register Y
     */
    private void execute8XY0(int i) {
        dataRegisters.write(
                X(i),
                dataRegisters.read(Y(i))
        );
    }

    /**
     * Adds the value of register Y to register X.
     * If the new value exceeds maximum unsigned byte size,
     * it is "wrapped around" with modulo and the carry
     * flag is set to 1 (0 otherwise).
     */
    private void execute8XY4(int i) {
        int newValue =
                unsigned(dataRegisters.read(X(i)))
                + unsigned(dataRegisters.read(Y(i)));

        int carry = 0;

        //  > 255 ? wrap around at 255+1 -> e.g. 256 = 0, 257 = 1, etc...
        if (newValue > UNSIGNED_BYTE_MAX_VALUE) {
            newValue %= UNSIGNED_BYTE_MAX_VALUE + 1;
            carry = 1;
        }

        dataRegisters.write(
                X(i),
                newValue
        );
        dataRegisters.write(CARRY_FLAG, carry);
    }

    /**
     * Reduce value of data register X by the value of data register Y.
     * If the new value is lower than zero (a borrow occurs), the carry flag
     * is set to 0 (1 otherwise).
     */
    private void execute8XY5(int i) {
        byte newValue =
                (byte) (
                        unsigned(dataRegisters.read(X(i)))
                        - unsigned(dataRegisters.read(Y(i)))
                );

        int carry = 1;

        if (newValue < 0) {
            carry = 0;
        }

        dataRegisters.write(
                X(i),
                newValue
        );

        dataRegisters.write(CARRY_FLAG, carry);
    }

    /**
     * sets address register to value NNN
     */
    private void executeANNN(int i) {
        addressRegister.write(i & GET_NNN);
    }

    /**
     * sets address register X to a random value
     * (between 0 and 255), which is masked with NN
     */
    private void executeCXNN(int i) {
        // the result of every bit operation in java is an
        // int, therefore we cast bit operations on bytes
        // to bytes again until the end of time :D
        dataRegisters.write(
                X(i),
                (int) (Math.random() * (255 + 1)) & (i & GET_NN)
        );
    }

    /**
     * draws a sprite at screen coordinates X,Y, using
     * N sprite rows found at currently registered address
     */
    private void executeDXYN(int i) {
        boolean pixelCollision = graphics.drawSprite(
                dataRegisters.read(X(i)),
                dataRegisters.read(Y(i)),
                memory.read(addressRegister.read(), i & GET_N)
        );

        dataRegisters.write(
                CARRY_FLAG,
                pixelCollision ? 1 : 0
        );
    }

    /**
     * Skip an instruction if the key of the key code, in
     * data register X, is being pressed
     */
    private void executeEX9E(int i) {
        if (keyboard.isKeyPressed(
                dataRegisters.read(X(i)))
        ) {
            programCounter.increment(2);
        }
    }

    /**
     * Skip an instruction if the key of the key code, in
     * data register X, is NOT being pressed
     */
    private void executeEXA1(int i) {
        if (!keyboard.isKeyPressed(
                dataRegisters.read(X(i)))
        ) {
            programCounter.increment(2);
        }
    }

    /**
     * sets data register X to the value of the delay timer
     */
    private void executeFX07(int i) {
        dataRegisters.write(
                X(i),
                delayTimer.read()
        );
    }

    /**
     * waits for a key press and stores
     * the key code in data register x
     */
    private void executeFX0A(int i) {
        dataRegisters.write(
                X(i),
                keyboard.waitForKey()
        );
    }

    /**
     * sets the delay timer to the value of data register X
     */
    private void executeFX15(int i) {
        delayTimer.write(
                dataRegisters.read(X(i))
        );
    }

    /**
     * adds value of data register X to the address register
     */
    private void executeFX1E(int i) {
        addressRegister.write(
                addressRegister.read()
                + dataRegisters.read(X(i))
        );
    }

    /**
     * sets the address register to the memory location of the
     * character corresponding to data register X's value
     */
    private void executeFX29(int i) {
        // the packaged font file contains the sprites for 0-F
        // in order from 0-F and was loaded to memory offset 0
        // each character sprite contains 5 bytes.
        // so the character B, for example, is at memory location 0xB * 5 = 55
        addressRegister.write((X(i)) * FONT_SIZE_IN_BYTES);
    }

    /**
     * sets three consecutive bytes of memory to the last,
     * middle and first digit of data register X's numerical value
     */
    private void executeFX33(int i) {
        int unsignedValue = unsigned(dataRegisters.read(X(i)));

        int memoryOffset = addressRegister.read();

        //example: 147
        memory.write(memoryOffset, (unsignedValue % 10));            // 7
        memory.write((memoryOffset + 1), (unsignedValue / 10 % 10)); // 4
        memory.write((memoryOffset + 2), (unsignedValue / 100));     // 1
    }

    /**
     * example:
     * <p>
     * first byte from memory : 00000111
     * second byte from memory: 01010010
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

        instruction |= unsigned(memory.read(++address));

        programCounter.increment((short) 2);

        return instruction;
    }

    private void throwInstructionException(int instruction) {
        throw new UnsupportedOperationException(
                "CPU instruction " + Integer.toHexString(instruction & 0xFFFF)
        );
    }
}