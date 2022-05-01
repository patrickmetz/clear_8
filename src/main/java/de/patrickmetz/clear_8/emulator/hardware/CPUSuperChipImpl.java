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
                     Registers dataRegisters, DelayTimer delayTimer,
                     Graphics gpu, Keyboard keyboard, Memory memory,
                     ProgramCounter programCounter, SoundTimer soundTimer) {
        super(addressRegister, dataRegisters, callStack, delayTimer,
                gpu, keyboard, memory, programCounter, soundTimer);
    }

    /**
     * Right-shifts the value of data register Y by one bit
     * (i.e. divides it by two) and stores it in register X
     * <p>
     * The former value's least significant bit
     * is stored in the carry register.
     * <p>
     *
     * @see CPUCosmacVipImpl#opcode8XY6(int)
     */
    protected void opcode8XY6(int opcode) {
        int value = registers.read(Y(opcode));

        registers.write(Registers.CARRY, LSB(value));
        registers.write(X(opcode), value >> 1);
    }

    /**
     * Sets X consecutive memory values to the values of the data
     * registers 0 to X, starting at the registered memory address.
     *
     * @see CPUCosmacVipImpl#opcodeFX55(int)
     */
    protected void opcodeFX55(int opcode) {
        int X       = X(opcode);
        int address = addressRegister.read();

        for (int register = 0; register <= X; register++) {
            memory.write(
                    address++,
                    registers.read(register)
            );
        }
    }

    /**
     * Sets data registers 0 to X to consecutive memory values,
     * starting at the registered memory address.
     *
     * @see CPUCosmacVipImpl#opcodeFX65(int)
     */
    protected void opcodeFX65(int opcode) {
        int X       = X(opcode);
        int address = addressRegister.read();

        for (int register = 0; register <= X; register++) {
            registers.write(
                    register,
                    memory.read(address++)
            );
        }
    }

    @Override
    protected void processNextInstruction() throws UnsupportedOperationException {
        int opcode = getNextOpcode();

        // compare first hex digit (four bits)
        switch (opcode & 0xF000) {
            case 0x0000:
                // compare last two hex digits (eight bits)
                switch (opcode & 0x00FF) {
                    case 0x00E0:
                        opcode00E0();
                        break;
                    case 0x00EE:
                        opcode00EE();
                        break;
                    default:
                        throwUnknownOpcodeException(opcode);
                }
                break;
            case 0x1000:
                opcode1NNN(opcode);
                break;
            case 0x2000:
                opcode2NNN(opcode);
                break;
            case 0x3000:
                opcode3XNN(opcode);
                break;
            case 0x4000:
                opcode4XNN(opcode);
                break;
            case 0x5000:
                opcode5XY0(opcode);
                break;
            case 0x6000:
                opcode6XNN(opcode);
                break;
            case 0x7000:
                opcode7XNN(opcode);
                break;
            case 0x8000:
                switch (opcode & 0x000F) {
                    case 0x0000:
                        opcode8XY0(opcode);
                        break;
                    case 0x0001:
                        opcode8XY1(opcode);
                        break;
                    case 0x0002:
                        opcode8XY2(opcode);
                        break;
                    case 0x0003:
                        opcode8XY3(opcode);
                        break;
                    case 0x0004:
                        opcode8XY4(opcode);
                        break;
                    case 0x0005:
                        opcode8XY5(opcode);
                        break;
                    case 0x0006:
                        opcode8XY6(opcode);
                        break;
                    case 0x0007:
                        opcode8XY7(opcode);
                        break;
                    case 0x000E:
                        opcode8XYE(opcode);
                        break;
                    default:
                        throwUnknownOpcodeException(opcode);
                }
                break;
            case 0x9000:
                opcode9XY0(opcode);
                break;
            case 0xA000:
                opcodeANNN(opcode);
                break;
            case 0xB000:
                opcodeBNNN(opcode);
                break;
            case 0xC000:
                opcodeCXNN(opcode);
                break;
            case 0xD000:
                opcodeDXYN(opcode);
                break;
            case 0xE000:
                switch (opcode & 0x000F) {
                    case 0x000E:
                        opcodeEX9E(opcode);
                        break;
                    case 0x0001:
                        opcodeEXA1(opcode);
                        break;
                    default:
                        throwUnknownOpcodeException(opcode);
                }
                break;
            case 0xF000:
                switch (opcode & 0x00FF) {
                    case 0x001E:
                        opcodeFX1E(opcode);
                        break;
                    case 0x0007:
                        opcodeFX07(opcode);
                        break;
                    case 0x000A:
                        opcodeFX0A(opcode);
                        break;
                    case 0x0015:
                        opcodeFX15(opcode);
                        break;
                    case 0x0018:
                        opcodeFX18(opcode);
                        break;
                    case 0x0029:
                        opcodeFX29(opcode);
                        break;
                    case 0x0033:
                        opcodeFX33(opcode);
                        break;
                    case 0x0055:
                        opcodeFX55(opcode);
                        break;
                    case 0x0065:
                        opcodeFX65(opcode);
                        break;
                    default:
                        throwUnknownOpcodeException(opcode);
                }
                break;
            default:
                throwUnknownOpcodeException(opcode);
        }
    }

    /**
     * Clears the screen.
     */
    private void opcode00E0() {
        gpu.clearDisplayData();
    }

    /**
     * Leaves subroutine to previous program counter position.
     */
    private void opcode00EE() {
        programCounter.write(callStack.pop());
    }

    /**
     * Sets program counter to value NNN.
     */
    private void opcode1NNN(int opcode) {
        programCounter.write(NNN(opcode));
    }

    /**
     * Enters subroutine and sets program counter to NNN.
     */
    private void opcode2NNN(int opcode) {
        callStack.push(programCounter.read());
        programCounter.write(NNN(opcode));
    }

    /**
     * Skips one instruction if the value of
     * data register X is equal to NN.
     */
    private void opcode3XNN(int opcode) {
        if (registers.read(X(opcode)) == NN(opcode)) {
            programCounter.increment();
        }
    }

    /**
     * Skip one instruction, if the value of data register X
     * is not equal to the value NN.
     */
    private void opcode4XNN(int opcode) {
        if (registers.read(X(opcode)) != NN(opcode)) {
            programCounter.increment();
        }
    }

    /**
     * Skip one instruction, if the value of data register X
     * is equal to the value of data register Y.
     */
    private void opcode5XY0(int opcode) {
        if (registers.read(X(opcode)) == registers.read(Y(opcode))) {
            programCounter.increment();
        }
    }

    /**
     * Sets data register X to value NN.
     */
    private void opcode6XNN(int opcode) {
        registers.write(X(opcode), NN(opcode));
    }

    /**
     * Adds the value NN to data register X.
     * If the new value exceeds maximum unsigned byte
     * size, it is "wrapped around" with modulo
     */
    private void opcode7XNN(int opcode) {
        int X = X(opcode);

        int result = registers.read(X) + NN(opcode);

        //  256 = 0, 257 = 1, 258 = 2, ...
        if (result > UNSIGNED_BYTE_MAX_VALUE) {
            result %= UNSIGNED_BYTE_MAX_VALUE + 1;
        }

        registers.write(X, result);
    }

    /**
     * Sets the value of data register X
     * to the value of data register Y.
     */
    private void opcode8XY0(int opcode) {
        registers.write(X(opcode), registers.read(Y(opcode)));
    }

    /**
     * Sets the value of data register X to the result of a
     * bitwise or of X's and Y's values.
     */
    private void opcode8XY1(int opcode) {
        int X = X(opcode);

        registers.write(
                X,
                registers.read(X)
                        | registers.read(Y(opcode))
        );
    }

    /**
     * Sets the value of data register X to the result of a
     * bitwise and of X's and Y's values.
     */
    private void opcode8XY2(int opcode) {
        int X = X(opcode);

        registers.write(
                X,
                registers.read(X)
                        & registers.read(Y(opcode))
        );
    }

    /**
     * Sets the value of data register X to the result of a
     * bitwise xor of X's and Y's values.
     */
    private void opcode8XY3(int opcode) {
        int X = X(opcode);

        registers.write(
                X,
                registers.read(X)
                        ^ registers.read(Y(opcode))
        );
    }

    /**
     * Adds the value of data register Y to X.
     * If the result exceeds maximum unsigned byte size,
     * it is "wrapped around" with modulo and the carry
     * flag is set to 1 (0 otherwise).
     */
    private void opcode8XY4(int opcode) {
        int result = unsignedByte(registers.read(X(opcode)))
                + unsignedByte(registers.read(Y(opcode)));

        int carry = 0;

        //  256 = 0, 257 = 1, 258 = 2, ...
        if (result > UNSIGNED_BYTE_MAX_VALUE) {
            result %= UNSIGNED_BYTE_MAX_VALUE + 1;
            carry = 1;
        }

        registers.write(X(opcode), result);
        registers.write(Registers.CARRY, carry);
    }

    /**
     * Subtracts data register Y's value from X's and stores the result
     * in X. If the result is negative (a borrow occurs), the carry flag
     * is set to 0 (1 otherwise).
     */
    private void opcode8XY5(int opcode) {
        int result = registers.read(X(opcode)) - registers.read(Y(opcode));

        // todo: should negative values really be 255? does it even matter?
        registers.write(X(opcode), (result < 0) ? 0xFF : result);

        registers.write(Registers.CARRY, (result < 0) ? 0 : 1);
    }

    /**
     * Subtracts data register X's value from Y's and stores the
     * result in X. If the result is negative (a borrow occurs),
     * the carry flag is set to 0 (1 otherwise).
     */
    private void opcode8XY7(int opcode) {
        int result = registers.read(Y(opcode)) - registers.read(X(opcode));

        // todo: should negative values really be 255? does it even matter?
        registers.write(X(opcode), (result < 0) ? 0xFF : result);

        registers.write(Registers.CARRY, (result < 0) ? 0 : 1);
    }

    /**
     * Sets the carry register to the most significant bit
     * of data register X.
     * <p>
     * Shifts data register X to the left by 1 bit.
     */
    private void opcode8XYE(int opcode) {
        int X = X(opcode);

        registers.write(Registers.CARRY, X & MSB);
        registers.write(
                X,
                registers.read(X) << 1
        );
    }

    /**
     * Skip one instruction if data register X
     * is not equal to data register Y.
     */
    private void opcode9XY0(int opcode) {
        if (
                registers.read(X(opcode))
                        != registers.read(Y(opcode))
        ) {
            programCounter.increment();
        }
    }

    /**
     * Set address register to value NNN.
     */
    private void opcodeANNN(int opcode) {
        addressRegister.write(NNN(opcode));
    }

    /**
     * Jump to the address made up of the sum of data
     * register 0's value and value NNN.
     * <p>
     * todo: implement different versions for VIP and SCHIP?
     */
    private void opcodeBNNN(int opcode) {
        programCounter.write(
                registers.read(0)
                        + NNN(opcode)
        );
    }

    /**
     * Set address register X to a random value
     * (between 0 and 255), which is masked with NN.
     */
    private void opcodeCXNN(int opcode) {
        registers.write(
                X(opcode),
                (int) (Math.random() * (255 + 1)) & NN(opcode)
        );
    }

    /**
     * Draws a sprite at screen coordinates X,Y, using
     * N sprite rows found at currently registered address.
     */
    private void opcodeDXYN(int opcode) {
        boolean pixelCollision = gpu.drawSprite(
                registers.read(X(opcode)),
                registers.read(Y(opcode)),
                memory.read(addressRegister.read(), N(opcode))
        );

        registers.write(
                Registers.CARRY,
                pixelCollision ? 1 : 0
        );
    }

    /**
     * Skips an instruction if the key, stored
     * in data register X, is being pressed.
     */
    private void opcodeEX9E(int opcode) {
        if (keyboard.isKeyPressed(
                registers.read(X(opcode))
        )) {
            programCounter.increment();
        }
    }

    /**
     * Skip an instruction if the key, stored in
     * data register X, is not being pressed.
     */
    private void opcodeEXA1(int opcode) {
        if (!keyboard.isKeyPressed(
                registers.read(X(opcode))
        )) {
            programCounter.increment();
        }
    }

    /**
     * Sets data register X to the value of the delay timer.
     */
    private void opcodeFX07(int opcode) {
        registers.write(X(opcode), delayTimer.read());
    }

    /**
     * Waits for a key to be pressed and stores
     * its key code in data register X.
     */
    private void opcodeFX0A(int opcode) {
        registers.write(X(opcode), keyboard.getNextPressedKey());
    }

    /**
     * Sets the delay timer to the value of data register X.
     */
    private void opcodeFX15(int opcode) {
        delayTimer.write(registers.read(X(opcode)));
    }

    /**
     * Sets the sound timer to the value of data register X.
     */
    private void opcodeFX18(int opcode) {
        soundTimer.write(registers.read(X(opcode)));
    }

    /**
     * Adds the value of data register X to the address register.
     */
    private void opcodeFX1E(int opcode) {
        addressRegister.write(
                addressRegister.read()
                        + registers.read(X(opcode))
        );
    }

    /**
     * Sets the address register to the memory location of the
     * character stored in data register X.
     */
    private void opcodeFX29(int opcode) {
        // the packaged font file contains the sprites for 0-F
        // in order from 0-F and was loaded to memory offset 0
        // each character sprite contains 5 bytes.
        // so the character B, for example, is at memory location 0xB * 5 = 55
        addressRegister.write((X(opcode)) * 5);
    }

    /**
     * Sets three consecutive memory addresses to the last, middle
     * and first digit of the numerical value of data register X.
     */
    private void opcodeFX33(int opcode) {
        int X       = unsignedByte(registers.read(X(opcode)));
        int address = addressRegister.read();

        //example value: 197
        memory.write(address, X % 10);          // 7
        memory.write(address + 1, X / 10 % 10); // 9
        memory.write(address + 2, X / 100);     // 1
    }

}