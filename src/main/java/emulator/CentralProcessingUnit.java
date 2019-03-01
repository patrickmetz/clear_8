/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 23:56.
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

    protected static final int CARRY = 0xF;
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

    protected static int N(int i) {
        return i & 0x000F;
    }

    protected static int NN(int i) {
        return i & 0x00FF;
    }

    protected static int NNN(int i) {
        return i & 0x0FFF;
    }

    protected static int X(int i) {
        return (i & 0x0F00) >> 8;
    }

    protected static int Y(int i) {
        return (i & 0x00F0) >> 4;
    }

    protected static int unsigned(int value) {
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
     * @see CentralProcessingUnitLegacy#opcode8XY6(int)
     */
    protected void opcode8XY6(int i) {
        int X = X(i);
        int value = dataRegisters.read(X);

        dataRegisters.write(CARRY, value & 1);
        dataRegisters.write(X, value >> 1);
    }

    /**
     * sets registers 0 to X to consecutive memory values
     * beginning at registered memory address
     *
     * @see CentralProcessingUnitLegacy#opcodeFX65(int)
     */
    protected void opcodeFX65(int i) {
        int X = X(i);
        int address = addressRegister.read();

        for (int j = 0; j <= X; j++) {
            dataRegisters.write(
                    j,
                    memory.read(address++)
            );
        }
    }

    void decrementTimers() {
        delayTimer.decrement();
        soundTimer.decrement();
    }

    void processNextInstruction() throws UnsupportedOperationException {
        int instruction = getNextInstruction();

        // check first hex digit
        switch (instruction & 0xF000) {
            case 0x0000:
                // check last two hex digits, etc...
                switch (instruction & 0x00FF) {
                    case 0x00E0:
                        opcode00E0(instruction);
                        break;
                    case 0x00EE:
                        opcode00EE(instruction);
                        break;
                    default:
                        throwInstructionException(instruction);
                }
                break;
            case 0x1000:
                opcode1NNN(instruction);
                break;
            case 0x2000:
                opcode2NNN(instruction);
                break;
            case 0x3000:
                opcode3XNN(instruction);
                break;
            case 0x4000:
                opcode4XNN(instruction);
                break;
            case 0x5000:
                opcode5XY0(instruction);
                break;
            case 0x6000:
                opcode6XNN(instruction);
                break;
            case 0x7000:
                opcode7XNN(instruction);
                break;
            case 0x8000:
                switch (instruction & 0x000F) {
                    case 0x0000:
                        opcode8XY0(instruction);
                        break;
                    case 0x0004:
                        opcode8XY4(instruction);
                        break;
                    case 0x0005:
                        opcode8XY5(instruction);
                        break;
                    case 0x0006:
                        opcode8XY6(instruction);
                        break;
                    default:
                        throwInstructionException(instruction);
                }
                break;
            case 0xA000:
                opcodeANNN(instruction);
                break;
            case 0xC000:
                opcodeCXNN(instruction);
                break;
            case 0xD000:
                opcodeDXYN(instruction);
                break;
            case 0xE000:
                switch (instruction & 0x000F) {
                    case 0x000E:
                        opcodeEX9E(instruction);
                        break;
                    case 0x0001:
                        opcodeEXA1(instruction);
                        break;
                    default:
                        throwInstructionException(instruction);
                }
                break;
            case 0xF000:
                switch (instruction & 0x00FF) {
                    case 0x001E:
                        opcodeFX1E(instruction);
                        break;
                    case 0x0007:
                        opcodeFX07(instruction);
                        break;
                    case 0x000A:
                        opcodeFX0A(instruction);
                        break;
                    case 0x0015:
                        opcodeFX15(instruction);
                        break;
                    case 0x0029:
                        opcodeFX29(instruction);
                        break;
                    case 0x0033:
                        opcodeFX33(instruction);
                        break;
                    case 0x0065:
                        opcodeFX65(instruction);
                        break;
                    default:
                        throwInstructionException(instruction);
                }
                break;
            default:
                throwInstructionException(instruction);
        }

    }

    void writeToMemory(int[] data, int address) {
        for (int d : data) {
            memory.write(address++, d);
        }
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

        programCounter.skipInstruction();

        return instruction;
    }

    /**
     * clears the screen
     */
    private void opcode00E0(int i) {
        graphics.clearScreen();
    }

    /**
     * leaving subroutine by popping former memory
     * location from the stack and jumping there
     */
    private void opcode00EE(int i) {
        programCounter.write(callStack.pop());
    }

    /**
     * sets program counter to value NNN
     */
    private void opcode1NNN(int i) {
        programCounter.write(NNN(i));
    }

    /**
     * execute subroutine by pushing current memory
     * location to the stack and jumping to the
     * subroutine's location
     */
    private void opcode2NNN(int i) {
        callStack.push(programCounter.read());
        programCounter.write(NNN(i));
    }

    /**
     * skips one instruction if the value of
     * data register X is equal to NN
     */
    private void opcode3XNN(int i) {
        if (dataRegisters.read(X(i)) == NN(i)) {
            programCounter.skipInstruction();
        }
    }

    /**
     * skip one instruction, if the value of data register X
     * is not equal to the value NN
     */
    private void opcode4XNN(int i) {
        if (dataRegisters.read(X(i)) != NN(i)) {
            programCounter.skipInstruction();
        }
    }

    /**
     * skip one instruction, if the value of data register X
     * is equal to the value of data register Y
     */
    private void opcode5XY0(int i) {
        if (dataRegisters.read(X(i)) == dataRegisters.read(Y(i))) {
            programCounter.skipInstruction();
        }
    }

    /**
     * sets data register X to value NN
     */
    private void opcode6XNN(int i) {
        dataRegisters.write(X(i), NN(i));
    }

    /**
     * Adds the value NN to data register X.
     * If the new value exceeds maximum unsigned byte size,
     * it is "wrapped around" with modulo, but no carry
     * flag is set.
     */
    private void opcode7XNN(int i) {
        int X = X(i);

        int result = unsigned(dataRegisters.read(X)) + NN(i);

        //  256 = 0, 257 = 1, ...
        if (result > UNSIGNED_BYTE_MAX_VALUE) {
            result %= UNSIGNED_BYTE_MAX_VALUE + 1;
        }

        dataRegisters.write(X, result);
    }

    /**
     * sets the value of data register X
     * to the value of data register Y
     */
    private void opcode8XY0(int i) {
        dataRegisters.write(X(i), dataRegisters.read(Y(i)));
    }

    /**
     * Adds the value of register Y to register X.
     * If the new value exceeds maximum unsigned byte size,
     * it is "wrapped around" with modulo and the carry
     * flag is set to 1 (0 otherwise).
     */
    private void opcode8XY4(int i) {
        int result =
                unsigned(dataRegisters.read(X(i)))
                + unsigned(dataRegisters.read(Y(i)));

        int carry = 0;

        // 256 = 0, 257 = 1, ...
        if (result > UNSIGNED_BYTE_MAX_VALUE) {
            result %= UNSIGNED_BYTE_MAX_VALUE + 1;
            carry = 1;
        }

        dataRegisters.write(X(i), result);
        dataRegisters.write(CARRY, carry);
    }

    /**
     * Reduce value of data register X by the value of data register Y.
     * If the new value is lower than zero (a borrow occurs), the carry flag
     * is set to 0 (1 otherwise).
     */
    private void opcode8XY5(int i) {
        byte newValue =
                (byte) (
                        unsigned(dataRegisters.read(X(i)))
                        - unsigned(dataRegisters.read(Y(i)))
                );

        dataRegisters.write(X(i), newValue);
        dataRegisters.write(
                CARRY,
                (newValue < 0) ? 0 : 1
        );
    }

    /**
     * sets address register to value NNN
     */
    private void opcodeANNN(int i) {
        addressRegister.write(NNN(i));
    }

    /**
     * sets address register X to a random value
     * (between 0 and 255), which is masked with NN
     */
    private void opcodeCXNN(int i) {
        dataRegisters.write(
                X(i),
                (int) (Math.random() * (255 + 1)) & NN(i)
        );
    }

    /**
     * draws a sprite at screen coordinates X,Y, using
     * N sprite rows found at currently registered address
     */
    private void opcodeDXYN(int i) {
        boolean pixelCollision = graphics.drawSprite(
                dataRegisters.read(X(i)),
                dataRegisters.read(Y(i)),
                memory.read(addressRegister.read(), N(i))
        );

        dataRegisters.write(
                CARRY,
                pixelCollision ? 1 : 0
        );
    }

    /**
     * Skip an instruction if the key of the key code, in
     * data register X, is being pressed
     */
    private void opcodeEX9E(int i) {
        if (keyboard.isKeyPressed(
                dataRegisters.read(X(i))
        )) {
            programCounter.skipInstruction();
        }
    }

    /**
     * Skip an instruction if the key of the key code, in
     * data register X, is NOT being pressed
     */
    private void opcodeEXA1(int i) {
        if (!keyboard.isKeyPressed(
                dataRegisters.read(X(i))
        )) {
            programCounter.skipInstruction();
        }
    }

    /**
     * sets data register X to the value of the delay timer
     */
    private void opcodeFX07(int i) {
        dataRegisters.write(X(i), delayTimer.read());
    }

    /**
     * waits for a key press and stores
     * the key code in data register x
     */
    private void opcodeFX0A(int i) {
        dataRegisters.write(X(i), keyboard.waitForKey());
    }

    /**
     * sets the delay timer to the value of data register X
     */
    private void opcodeFX15(int i) {
        delayTimer.write(dataRegisters.read(X(i)));
    }

    /**
     * adds value of data register X to the address register
     */
    private void opcodeFX1E(int i) {
        addressRegister.write(
                addressRegister.read()
                + dataRegisters.read(X(i))
        );
    }

    /**
     * sets the address register to the memory location of the
     * character corresponding to data register X's value
     */
    private void opcodeFX29(int i) {
        // the packaged font file contains the sprites for 0-F
        // in order from 0-F and was loaded to memory offset 0
        // each character sprite contains 5 bytes.
        // so the character B, for example, is at memory location 0xB * 5 = 55
        addressRegister.write((X(i)) * 5);
    }

    /**
     * sets three consecutive bytes of memory to the last,
     * middle and first digit of data register X's numerical value
     */
    private void opcodeFX33(int i) {
        int X = unsigned(dataRegisters.read(X(i)));
        int address = addressRegister.read();

        //example: 137
        memory.write(address, X % 10);          // 7
        memory.write(address + 1, X / 10 % 10); // 3
        memory.write(address + 2, X / 100);     // 1
    }

    private void throwInstructionException(int instruction) {
        throw new UnsupportedOperationException(
                "CPU instruction " + Integer.toHexString(instruction & 0xFFFF)
        );
    }
}