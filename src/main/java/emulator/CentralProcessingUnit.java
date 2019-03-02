/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 02.03.19 15:16.
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

    /**
     * The address of the carry register.
     * <p>
     * <p>
     * A carry is used to signal that a value overflows
     * its range, i.e. an unsigned byte bigger than 255
     * (which is than actually wrapped around with the
     * modulo operator, i.e. 256 becomes 0, 257 becomes
     * 1, etc...).
     * <p>
     * <p>
     * chip8 also uses the carry to signal borrows, i.e.
     * value underflows.
     */
    protected static final int CARRY = 0xF;

    /**
     * Bitmask to get the first bit of a byte
     */
    protected static final int LEAST_SIGNIFICANT_BIT = 0b0000_0001;

    /**
     * Bitmask to get the last bit of a byte
     */
    protected static final int MOST_SIGNIFICANT_BIT = 0b1000_0000;

    /**
     * The maximum numerical value an unsigned byte can hold.
     */
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

    /**
     * Retrieves the rightmost hex digit (four bits) from an opcode.
     */
    protected static int N(int o) {
        return o & 0x000F;
    }

    /**
     * Retrieves the rightmost two hex digits (eight bits) from an opcode.
     */
    protected static int NN(int o) {
        return o & 0x00FF;
    }

    /**
     * Retrieves the rightmost three hex digits (twelve bits) from an opcode.
     */
    protected static int NNN(int o) {
        return o & 0x0FFF;
    }

    /**
     * Retrieves the X variable (bits 9 to 12) from an opcode.
     */
    protected static int X(int o) {
        return (o & 0x0F00) >> 8;
    }

    /**
     * Retrieves the Y variable (bits 5 to 8) from an opcode.
     */
    protected static int Y(int o) {
        return (o & 0x00F0) >> 4;
    }

    /**
     * Retrieves the rightmost eight bits of a whole number,
     * in order to get a value from within an unsigned byte's
     * value range. Just make sure to put the result into a
     * whole number type which is bigger than a byte.
     * <p>
     * <p>
     * chip8 actually works with unsigned bytes (0 to 255),
     * but Java interprets those values as signed (-128 to 127).
     * <p>
     * <p>
     * When we need to correctly interpret the meaning of a whole
     * numbers bits (i.e. the value chip8 would assume) we use this
     * method.
     * <p>
     * <p>
     * This emulator stores the bytes of chip8 programs into
     * integers. So the following example is equivalent of reading
     * a byte from memory.
     * <p>
     * <p>
     * int a = memory.read(someAddress);<p>
     * a is -32 or 11111111_11111111_11111111_11100000
     * <p>
     * <p>
     * int b = unsigned(a);<p>
     * b is 224 or 00000000_00000000_00000000_11100000
     * <p>
     * <p>
     * but don't put the result in an actual byte,
     * or you'll get a signed value again:
     * <p>
     * byte c = (byte) unsigned(a);<p>
     * c = -32 or 11100000
     */
    protected static int unsigned(int value) {
        return value & 0xFF;
    }

    /**
     * Shifts the value of data register X by one bit
     * to the right (i.e. divides it by two).
     * <p>
     * The former value's least significant bit
     * is stored in the carry register.
     * <p>
     *
     * @see CentralProcessingUnitLegacy#opcode8XY6(int)
     */
    protected void opcode8XY6(int o) {
        int X = X(o);
        int value = dataRegisters.read(X);

        dataRegisters.write(CARRY, value & LEAST_SIGNIFICANT_BIT);
        dataRegisters.write(X, value >> 1);
    }

    /**
     * Sets X consecutive memory values to the values of the data
     * registers 0 to X, starting at the registered memory address.
     *
     * @see CentralProcessingUnitLegacy#opcodeFX55(int)
     */
    protected void opcodeFX55(int o) {
        int X = X(o);
        int address = addressRegister.read();

        for (int register = 0; register <= X; register++) {
            memory.write(
                    address++,
                    dataRegisters.read(register)
            );
        }
    }

    /**
     * Sets data registers 0 to X to consecutive memory values,
     * starting at the registered memory address.
     *
     * @see CentralProcessingUnitLegacy#opcodeFX65(int)
     */
    protected void opcodeFX65(int o) {
        int X = X(o);
        int address = addressRegister.read();

        for (int register = 0; register <= X; register++) {
            dataRegisters.write(
                    register,
                    memory.read(address++)
            );
        }
    }

    /**
     * Decreases both timers.
     */
    void decrementTimers() {
        delayTimer.decrement();
        soundTimer.decrement();
    }

    /**
     * Executes the next CPU instruction from memory.
     */
    void processNextOpcode() throws UnsupportedOperationException {
        int o = getNextOpcode();

        // compare first hex digit (four bits)
        switch (o & 0xF000) {
            case 0x0000:
                // compare last two hex digits (eight bits)
                switch (o & 0x00FF) {
                    case 0x00E0:
                        opcode00E0();
                        break;
                    case 0x00EE:
                        opcode00EE();
                        break;
                    default:
                        throwInstructionException(o);
                }
                break;
            case 0x1000:
                opcode1NNN(o);
                break;
            case 0x2000:
                opcode2NNN(o);
                break;
            case 0x3000:
                opcode3XNN(o);
                break;
            case 0x4000:
                opcode4XNN(o);
                break;
            case 0x5000:
                opcode5XY0(o);
                break;
            case 0x6000:
                opcode6XNN(o);
                break;
            case 0x7000:
                opcode7XNN(o);
                break;
            case 0x8000:
                switch (o & 0x000F) {
                    case 0x0000:
                        opcode8XY0(o);
                        break;
                    case 0x0001:
                        opcode8XY1(o);
                        break;
                    case 0x0002:
                        opcode8XY2(o);
                        break;
                    case 0x0003:
                        opcode8XY3(o);
                        break;
                    case 0x0004:
                        opcode8XY4(o);
                        break;
                    case 0x0005:
                        opcode8XY5(o);
                        break;
                    case 0x0006:
                        opcode8XY6(o);
                        break;
                    case 0x0007:
                        opcode8XY7(o);
                        break;
                    case 0x000E:
                        opcode8XYE(o);
                        break;
                    default:
                        throwInstructionException(o);
                }
                break;
            case 0x9000:
                opcode9XY0(o);
                break;
            case 0xA000:
                opcodeANNN(o);
                break;
            case 0xC000:
                opcodeCXNN(o);
                break;
            case 0xD000:
                opcodeDXYN(o);
                break;
            case 0xE000:
                switch (o & 0x000F) {
                    case 0x000E:
                        opcodeEX9E(o);
                        break;
                    case 0x0001:
                        opcodeEXA1(o);
                        break;
                    default:
                        throwInstructionException(o);
                }
                break;
            case 0xF000:
                switch (o & 0x00FF) {
                    case 0x001E:
                        opcodeFX1E(o);
                        break;
                    case 0x0007:
                        opcodeFX07(o);
                        break;
                    case 0x000A:
                        opcodeFX0A(o);
                        break;
                    case 0x0015:
                        opcodeFX15(o);
                        break;
                    case 0x0029:
                        opcodeFX29(o);
                        break;
                    case 0x0033:
                        opcodeFX33(o);
                        break;
                    case 0x0055:
                        opcodeFX55(o);
                        break;
                    case 0x0065:
                        opcodeFX65(o);
                        break;
                    default:
                        throwInstructionException(o);
                }
                break;
            default:
                throwInstructionException(o);
        }
    }

    /**
     * Writes a data field to memory.
     */
    void writeToMemory(int[] data, int address) {
        for (int d : data) {
            memory.write(address++, unsigned(d));
        }
    }

    /**
     * Constructs the next opcode to be executed.
     * <p>
     * <p>
     * Opcodes have a size of 16 bit. So we fetch
     * 2 bytes from memory to build them.
     * <p>
     * <p>
     * Example:
     * <p>
     * first byte from memory : 00000111
     * <p>
     * second byte from memory: 01010010
     * <p>
     * <p>
     * opcode               : 00000000|00000000<p>
     * opcode = first byte  : 00000000|00000111<p>
     * opcode <<= 8         : 00000111|00000000<p>
     * opcode |= second byte: 00000111|01010010
     */
    private int getNextOpcode() {
        int address = programCounter.read();

        int opcode = memory.read(address);
        opcode <<= 8;

        opcode |= unsigned(memory.read(++address));

        programCounter.skipOpcode();

        return opcode;
    }

    /**
     * Clears the screen.
     */
    private void opcode00E0() {
        graphics.clearScreen();
    }

    /**
     * Leaves subroutine.
     */
    private void opcode00EE() {
        programCounter.write(callStack.pop());
    }

    /**
     * Sets program counter to value NNN.
     */
    private void opcode1NNN(int o) {
        programCounter.write(NNN(o));
    }

    /**
     * Enters subroutine.
     */
    private void opcode2NNN(int o) {
        callStack.push(programCounter.read());
        programCounter.write(NNN(o));
    }

    /**
     * Skips one instruction if the value of
     * data register X is equal to NN.
     */
    private void opcode3XNN(int o) {
        if (dataRegisters.read(X(o)) == NN(o)) {
            programCounter.skipOpcode();
        }
    }

    /**
     * Skip one instruction, if the value of data register X
     * is not equal to the value NN.
     */
    private void opcode4XNN(int o) {
        if (dataRegisters.read(X(o)) != NN(o)) {
            programCounter.skipOpcode();
        }
    }

    /**
     * Skip one instruction, if the value of data register X
     * is equal to the value of data register Y.
     */
    private void opcode5XY0(int o) {
        if (dataRegisters.read(X(o)) == dataRegisters.read(Y(o))) {
            programCounter.skipOpcode();
        }
    }

    /**
     * Sets data register X to value NN.
     */
    private void opcode6XNN(int o) {
        dataRegisters.write(X(o), NN(o));
    }

    /**
     * Adds the value NN to data register X.
     * If the new value exceeds maximum unsigned byte
     * size, it is "wrapped around" with modulo, but
     * no carry flag is set.
     */
    private void opcode7XNN(int o) {
        int X = X(o);

        int result = unsigned(dataRegisters.read(X)) + NN(o);

        //  256 = 0, 257 = 1, 258 = 2, ...
        if (result > UNSIGNED_BYTE_MAX_VALUE) {
            result %= UNSIGNED_BYTE_MAX_VALUE + 1;
        }

        dataRegisters.write(X, result);
    }

    /**
     * Sets the value of data register X
     * to the value of data register Y.
     */
    private void opcode8XY0(int o) {
        dataRegisters.write(X(o), dataRegisters.read(Y(o)));
    }

    /**
     * Sets the value of data register X to the result of a
     * bitwise or of X's and Y's values.
     */
    private void opcode8XY1(int o) {
        int X = X(o);

        dataRegisters.write(
                X,
                dataRegisters.read(X)
                | dataRegisters.read(Y(o))
        );
    }

    /**
     * Sets the value of data register X to the result of a
     * bitwise and of X's and Y's values.
     */
    private void opcode8XY2(int o) {
        int X = X(o);

        dataRegisters.write(
                X,
                dataRegisters.read(X)
                & dataRegisters.read(Y(o))
        );
    }

    /**
     * Sets the value of data register X to the result of a
     * bitwise xor of X's and Y's values.
     */
    private void opcode8XY3(int o) {
        int X = X(o);

        dataRegisters.write(
                X,
                dataRegisters.read(X)
                ^ dataRegisters.read(Y(o))
        );
    }

    /**
     * Adds the value of data register Y to X.
     * If the result exceeds maximum unsigned byte size,
     * it is "wrapped around" with modulo and the carry
     * flag is set to 1 (0 otherwise).
     */
    private void opcode8XY4(int o) {
        int result =
                unsigned(dataRegisters.read(X(o)))
                + unsigned(dataRegisters.read(Y(o)));

        int carry = 0;

        //  256 = 0, 257 = 1, 258 = 2, ...
        if (result > UNSIGNED_BYTE_MAX_VALUE) {
            result %= UNSIGNED_BYTE_MAX_VALUE + 1;
            carry = 1;
        }

        dataRegisters.write(X(o), result);
        dataRegisters.write(CARRY, carry);
    }

    /**
     * Subtracts data register Y's value from X's and stores the result
     * in X. If the result is negative (a borrow occurs), the carry flag
     * is set to 0 (1 otherwise).
     */
    private void opcode8XY5(int o) {
        int result =
                unsigned(dataRegisters.read(X(o)))
                - unsigned(dataRegisters.read(Y(o)));

        dataRegisters.write(X(o), result);
        dataRegisters.write(
                CARRY,
                (result < 0) ? 0 : 1
        );
    }

    /**
     * Subtracts data register X's value from Y's and stores the
     * result in X. If the result is negative (a borrow occurs),
     * the carry flag is set to 0 (1 otherwise).
     */
    private void opcode8XY7(int o) {
        int result =
                unsigned(dataRegisters.read(Y(o)))
                - unsigned(dataRegisters.read(X(o)));

        dataRegisters.write(X(o), result);
        dataRegisters.write(
                CARRY,
                (result < 0) ? 0 : 1
        );
    }

    /**
     * Sets the carry register to the most significant bit
     * of data register X.
     * <p>
     * Shifts data register X to the left by 1 bit.
     */
    private void opcode8XYE(int o) {
        int X = X(o);

        dataRegisters.write(CARRY, X & MOST_SIGNIFICANT_BIT);
        dataRegisters.write(
                X,
                dataRegisters.read(X) << 1
        );
    }

    /**
     * Skip one instruction if data register X
     * is not equal to data register Y.
     */
    private void opcode9XY0(int o) {
        if (
                dataRegisters.read(X(o))
                != dataRegisters.read(Y(o))
        ) {
            programCounter.skipOpcode();
        }
    }

    /**
     * Set address register to value NNN.
     */
    private void opcodeANNN(int o) {
        addressRegister.write(NNN(o));
    }

    /**
     * Set address register X to a random value
     * (between 0 and 255), which is masked with NN.
     */
    private void opcodeCXNN(int o) {
        dataRegisters.write(
                X(o),
                (int) (Math.random() * (255 + 1)) & NN(o)
        );
    }

    /**
     * Draws a sprite at screen coordinates X,Y, using
     * N sprite rows found at currently registered address.
     */
    private void opcodeDXYN(int o) {
        boolean pixelCollision = graphics.drawSprite(
                dataRegisters.read(X(o)),
                dataRegisters.read(Y(o)),
                memory.read(addressRegister.read(), N(o))
        );

        dataRegisters.write(
                CARRY,
                pixelCollision ? 1 : 0
        );
    }

    /**
     * Skips an instruction if the key, stored
     * in data register X, is being pressed.
     */
    private void opcodeEX9E(int o) {
        if (keyboard.isKeyPressed(
                dataRegisters.read(X(o))
        )) {
            programCounter.skipOpcode();
        }
    }

    /**
     * Skip an instruction if the key, stored in
     * data register X, is not being pressed.
     */
    private void opcodeEXA1(int o) {
        if (!keyboard.isKeyPressed(
                dataRegisters.read(X(o))
        )) {
            programCounter.skipOpcode();
        }
    }

    /**
     * Sets data register X to the value of the delay timer.
     */
    private void opcodeFX07(int o) {
        dataRegisters.write(X(o), delayTimer.read());
    }

    /**
     * Waits for a key to be pressed and stores
     * its key code in data register X.
     */
    private void opcodeFX0A(int o) {
        dataRegisters.write(X(o), keyboard.waitForKey());
    }

    /**
     * Sets the delay timer to the value of data register X.
     */
    private void opcodeFX15(int o) {
        delayTimer.write(dataRegisters.read(X(o)));
    }

    /**
     * Adds the value of data register X to the address register.
     */
    private void opcodeFX1E(int o) {
        addressRegister.write(
                addressRegister.read()
                + dataRegisters.read(X(o))
        );
    }

    /**
     * Sets the address register to the memory location of the
     * character stored in data register X.
     */
    private void opcodeFX29(int o) {
        // the packaged font file contains the sprites for 0-F
        // in order from 0-F and was loaded to memory offset 0
        // each character sprite contains 5 bytes.
        // so the character B, for example, is at memory location 0xB * 5 = 55
        addressRegister.write((X(o)) * 5);
    }

    /**
     * Sets three consecutive memory addresses to the last, middle
     * and first digit of the numerical value of data register X.
     */
    private void opcodeFX33(int o) {
        int X = unsigned(dataRegisters.read(X(o)));
        int address = addressRegister.read();

        //example value: 197
        memory.write(address, X % 10);          // 7
        memory.write(address + 1, X / 10 % 10); // 9
        memory.write(address + 2, X / 100);     // 1
    }

    private void throwInstructionException(int instruction) {
        throw new UnsupportedOperationException(
                "CPU instruction " + Integer.toHexString(instruction & 0xFFFF)
        );
    }
}