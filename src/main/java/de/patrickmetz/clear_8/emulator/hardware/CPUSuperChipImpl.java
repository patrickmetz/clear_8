package de.patrickmetz.clear_8.emulator.hardware;

import de.patrickmetz.clear_8.emulator.input.Keyboard;

/**
 * CPU implementation using opcodes of the SCHIP (Super Chip)
 * <p>
 * see:
 * https://github.com/Chromatophore/HP48-Superchip#behavior-and-quirk-investigations
 */
class CPUSuperChipImpl extends AbstractCPU {

    CPUSuperChipImpl(AddressRegister addressRegister, CallStack callStack,
                     DataRegisters dataRegisters, DelayTimer delayTimer,
                     Graphics gpu, Keyboard keyboard, Memory memory,
                     ProgramCounter programCounter, SoundTimer soundTimer) {
        super(addressRegister, dataRegisters, callStack, delayTimer,
                gpu, keyboard, memory, programCounter, soundTimer);
    }

    @Override
    protected void processNextInstruction() throws UnsupportedOperationException {
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
                        throwUnknownOpcodeException(o);
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
                        throwUnknownOpcodeException(o);
                }
                break;
            case 0x9000:
                opcode9XY0(o);
                break;
            case 0xA000:
                opcodeANNN(o);
                break;
            case 0xB000:
                opcodeBNNN(o);
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
                        throwUnknownOpcodeException(o);
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
                    case 0x0018:
                        opcodeFX18(o);
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
                        throwUnknownOpcodeException(o);
                }
                break;
            default:
                throwUnknownOpcodeException(o);
        }
    }


    /**
     * Shifts the value of data register X by one bit
     * to the right (i.e. divides it by two).
     * <p>
     * The former value's least significant bit
     * is stored in the carry register.
     * <p>
     *
     * @see CPUCosmacVipImpl#opcode8XY6(int)
     */
    protected void opcode8XY6(int o) {
        int X     = x(o);
        int value = dataRegisters.read(X);

        dataRegisters.write(CARRY, value & LSB);
        dataRegisters.write(X, value >> 1);
    }

    /**
     * Sets X consecutive memory values to the values of the data
     * registers 0 to X, starting at the registered memory address.
     *
     * @see CPUCosmacVipImpl#opcodeFX55(int)
     */
    protected void opcodeFX55(int o) {
        int X       = x(o);
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
     * @see CPUCosmacVipImpl#opcodeFX65(int)
     */
    protected void opcodeFX65(int o) {
        int X       = x(o);
        int address = addressRegister.read();

        for (int register = 0; register <= X; register++) {
            dataRegisters.write(
                    register,
                    memory.read(address++)
            );
        }
    }

    /**
     * Clears the screen.
     */
    private void opcode00E0() {
        gpu.clearDisplayData();
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
        programCounter.write(nnn(o));
    }

    /**
     * Enters subroutine.
     */
    private void opcode2NNN(int o) {
        callStack.push(programCounter.read());
        programCounter.write(nnn(o));
    }

    /**
     * Skips one instruction if the value of
     * data register X is equal to NN.
     */
    private void opcode3XNN(int o) {
        if (dataRegisters.read(x(o)) == nn(o)) {
            programCounter.increment(OPCODE_SIZE);
        }
    }

    /**
     * Skip one instruction, if the value of data register X
     * is not equal to the value NN.
     */
    private void opcode4XNN(int o) {
        if (dataRegisters.read(x(o)) != nn(o)) {
            programCounter.increment(OPCODE_SIZE);
        }
    }

    /**
     * Skip one instruction, if the value of data register X
     * is equal to the value of data register Y.
     */
    private void opcode5XY0(int o) {
        if (dataRegisters.read(x(o)) == dataRegisters.read(y(o))) {
            programCounter.increment(OPCODE_SIZE);
        }
    }

    /**
     * Sets data register X to value NN.
     */
    private void opcode6XNN(int o) {
        dataRegisters.write(x(o), nn(o));
    }

    /**
     * Adds the value NN to data register X.
     * If the new value exceeds maximum unsigned byte
     * size, it is "wrapped around" with modulo, but
     * no carry flag is set.
     */
    private void opcode7XNN(int o) {
        int X = x(o);

        int result = unsigned(dataRegisters.read(X)) + nn(o);

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
        dataRegisters.write(x(o), dataRegisters.read(y(o)));
    }

    /**
     * Sets the value of data register X to the result of a
     * bitwise or of X's and Y's values.
     */
    private void opcode8XY1(int o) {
        int X = x(o);

        dataRegisters.write(
                X,
                dataRegisters.read(X)
                        | dataRegisters.read(y(o))
        );
    }

    /**
     * Sets the value of data register X to the result of a
     * bitwise and of X's and Y's values.
     */
    private void opcode8XY2(int o) {
        int X = x(o);

        dataRegisters.write(
                X,
                dataRegisters.read(X)
                        & dataRegisters.read(y(o))
        );
    }

    /**
     * Sets the value of data register X to the result of a
     * bitwise xor of X's and Y's values.
     */
    private void opcode8XY3(int o) {
        int X = x(o);

        dataRegisters.write(
                X,
                dataRegisters.read(X)
                        ^ dataRegisters.read(y(o))
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
                unsigned(dataRegisters.read(x(o)))
                        + unsigned(dataRegisters.read(y(o)));

        int carry = 0;

        //  256 = 0, 257 = 1, 258 = 2, ...
        if (result > UNSIGNED_BYTE_MAX_VALUE) {
            result %= UNSIGNED_BYTE_MAX_VALUE + 1;
            carry = 1;
        }

        dataRegisters.write(x(o), result);
        dataRegisters.write(CARRY, carry);
    }

    /**
     * Subtracts data register Y's value from X's and stores the result
     * in X. If the result is negative (a borrow occurs), the carry flag
     * is set to 0 (1 otherwise).
     */
    private void opcode8XY5(int o) {
        int result =
                unsigned(dataRegisters.read(x(o)))
                        - unsigned(dataRegisters.read(y(o)));

        dataRegisters.write(x(o), result);
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
                unsigned(dataRegisters.read(y(o)))
                        - unsigned(dataRegisters.read(x(o)));

        dataRegisters.write(x(o), result);
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
        int X = x(o);

        dataRegisters.write(CARRY, X & MSB);
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
                dataRegisters.read(x(o))
                        != dataRegisters.read(y(o))
        ) {
            programCounter.increment(OPCODE_SIZE);
        }
    }

    /**
     * Set address register to value NNN.
     */
    private void opcodeANNN(int o) {
        addressRegister.write(nnn(o));
    }

    /**
     * Jump to the address made up of the sum of data
     * register 0's value and value NNN.
     * <p>
     * todo: implement different versions for VIP and SCHIP?
     */
    private void opcodeBNNN(int o) {
        programCounter.write(
                dataRegisters.read(0)
                        + nnn(o)
        );
    }

    /**
     * Set address register X to a random value
     * (between 0 and 255), which is masked with NN.
     */
    private void opcodeCXNN(int o) {
        dataRegisters.write(
                x(o),
                (int) (Math.random() * (255 + 1)) & nn(o)
        );
    }

    /**
     * Draws a sprite at screen coordinates X,Y, using
     * N sprite rows found at currently registered address.
     */
    private void opcodeDXYN(int o) {
        boolean pixelCollision = gpu.drawSprite(
                dataRegisters.read(x(o)),
                dataRegisters.read(y(o)),
                memory.read(addressRegister.read(), n(o))
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
                dataRegisters.read(x(o))
        )) {
            programCounter.increment(OPCODE_SIZE);
        }
    }

    /**
     * Skip an instruction if the key, stored in
     * data register X, is not being pressed.
     */
    private void opcodeEXA1(int o) {
        if (!keyboard.isKeyPressed(
                dataRegisters.read(x(o))
        )) {
            programCounter.increment(OPCODE_SIZE);
        }
    }

    /**
     * Sets data register X to the value of the delay timer.
     */
    private void opcodeFX07(int o) {
        dataRegisters.write(x(o), delayTimer.read());
    }

    /**
     * Waits for a key to be pressed and stores
     * its key code in data register X.
     */
    private void opcodeFX0A(int o) {
        dataRegisters.write(x(o), keyboard.getNextPressedKey());
    }

    /**
     * Sets the delay timer to the value of data register X.
     */
    private void opcodeFX15(int o) {
        delayTimer.write(dataRegisters.read(x(o)));
    }

    /**
     * Sets the sound timer to the value of data register X.
     */
    private void opcodeFX18(int o) {
        soundTimer.write(dataRegisters.read(x(o)));
    }

    /**
     * Adds the value of data register X to the address register.
     */
    private void opcodeFX1E(int o) {
        addressRegister.write(
                addressRegister.read()
                        + dataRegisters.read(x(o))
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
        addressRegister.write((x(o)) * 5);
    }

    /**
     * Sets three consecutive memory addresses to the last, middle
     * and first digit of the numerical value of data register X.
     */
    private void opcodeFX33(int o) {
        int X       = unsigned(dataRegisters.read(x(o)));
        int address = addressRegister.read();

        //example value: 197
        memory.write(address, X % 10);          // 7
        memory.write(address + 1, X / 10 % 10); // 9
        memory.write(address + 2, X / 100);     // 1
    }

}