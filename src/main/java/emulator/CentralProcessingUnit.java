/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 28.02.19 16:24.
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
     * modifies the value of data register X
     * by shifting it to the right by one bit
     * (equivalent to dividing by two).
     * <p>
     * the least significant bit of the
     * former value is stored in data register F
     * <p>
     *
     * @see CentralProcessingUnitLegacy#execute8XY6(short)
     */
    protected void execute8XY6(short i) {
        int xAddress = (i & 0x0F00) >> 8;
        byte xValue = dataRegisters.read((byte) xAddress);

        dataRegisters.write((byte) 0xF, (byte) (xValue & 1));
        dataRegisters.write((byte) xAddress, (byte) (xValue >> 1));
    }

    /**
     * sets registers 0 to X to consecutive memory values
     * beginning at registered memory address
     *
     * @see CentralProcessingUnitLegacy#executeFX65(short)
     */
    protected void executeFX65(short i) {
        byte endRegister = (byte) ((i & 0x0F00) >> 8);
        short memoryOffset = addressRegister.read();

        for (byte j = 0; j <= endRegister; j++) {
            dataRegisters.write(
                    j,
                    memory.read(memoryOffset++)
            );
        }
    }

    /**
     * example:
     * <p>
     * instruction = 4645 as integer
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
        short instruction = getNextInstruction();

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
            case 0xF000:
                switch (instruction & 0x00FF) {
                    case 0x001E:
                        executeFX1E(instruction);
                        break;
                    case 0x000A:
                        executeFX0A(instruction);
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

    void writeToMemory(byte[] bytes, int offset) {
        for (byte b : bytes) {
            memory.write((short) offset++, b);
        }
    }

    /**
     * clears the screen
     */
    private void execute00E0(short i) {
        graphics.clearScreen();
    }

    /**
     * leaving subroutine by popping former memory
     * location from the stack and jumping there
     */
    private void execute00EE(short i) {
        programCounter.write(callStack.pop());
    }

    /**
     * sets program counter to value NNN
     */
    private void execute1NNN(short i) {
        programCounter.write((short) (i & 0x0FFF));
    }

    /**
     * execute subroutine by pushing current memory
     * location to the stack and jumping to the
     * subroutine's location
     */
    private void execute2NNN(short i) {
        callStack.push(programCounter.read());
        programCounter.write((short) (i & 0x0FFF));
    }

    /**
     * skips one instruction if the value of
     * data register X is equal to NN
     */
    private void execute3XNN(short i) {
        if (
                dataRegisters.read((byte) ((i & 0x0F00) >> 8))
                == (i & 0x00FF)
        ) {
            // one instruction = 2 bytes
            programCounter.increment((short) 2);
        }
    }

    /**
     * sets data register X to value NN
     */
    private void execute6XNN(short i) {
        dataRegisters.write(
                (byte) ((i & 0x0F00) >> 8),
                (byte) (i & 0x00FF)
        );
    }

    /**
     * adds value NN to data register X.
     * if the new value exceeds maximum unsigned byte size,
     * it is "wrapped around" (modulo) without setting carry flag.
     */
    private void execute7XNN(short i) {
        byte address = (byte) ((i & 0x0F00) >> 8);
        int newValue = dataRegisters.read(address) + (i & 0x00FF);

        if (newValue > 255) {
            newValue %= 255;
        }

        dataRegisters.write(address, (byte) newValue);
    }

    /**
     * sets the value of data register X
     * to the value of data register Y
     */
    private void execute8XY0(short i) {
        dataRegisters.write(
                (byte) ((i & 0x0F00) >> 8),
                dataRegisters.read((byte) ((i & 0x00F0) >> 4))
        );
    }

    /**
     * sets address register to value NNN
     */
    private void executeANNN(short i) {
        addressRegister.write((short) (i & 0x0FFF));
    }

    /**
     * sets address register X to a random value
     * (between 0 and 255), which is masked with NN
     */
    private void executeCXNN(short i) {
        // the result of every bit operation in java is an
        // integer, therefore we cast bit operations on bytes
        // to bytes again until the end of time :D
        dataRegisters.write(
                (byte) ((i & 0x0F00) >> 8),                // register X
                (byte) (
                        (byte) (Math.random() * (255 + 1)) // random value
                        & (byte) (i & 0x00FF)              // mask NN
                )
        );
    }

    /**
     * draws a sprite at screen coordinates X,Y, using
     * N sprite rows found at currently registered address
     */
    private void executeDXYN(short i) {
        boolean pixelCollision = graphics.drawSprite(
                dataRegisters.read((byte) ((i & 0x0F00) >> 8)),
                dataRegisters.read((byte) ((i & 0x00F0) >> 4)),
                memory.read(addressRegister.read(), (i) & 0x000F)
        );

        dataRegisters.write(
                (byte) 0xF,
                (byte) (pixelCollision ? 1 : 0)
        );
    }

    /**
     * waits for a key press and stores
     * the key code in data register x
     */
    private void executeFX0A(short i) {
        dataRegisters.write(
                (byte) ((i & 0x0F00) >> 8),
                keyboard.waitForKey()
        );
    }

    /**
     * adds value of data register X to the address register
     */
    private void executeFX1E(short i) {
        addressRegister.write(
                (short) (
                        addressRegister.read()
                        + dataRegisters.read((byte) ((i & 0x0F00) >> 8))
                )
        );
    }

    /**
     * sets the address register to the memory location of the
     * character corresponding to data register X's value
     */
    private void executeFX29(short i) {
        // the packaged font file contains the sprites for 0-F
        // in order from 0-F and was loaded to memory offset 0
        // each character sprite contains 5 bytes.
        // so the character B, for example, is at memory location 0xB * 5 = 55
        addressRegister.write((short) (((i & 0x0F00) >> 8) * 5));
    }

    /**
     * sets three consecutive bytes of memory to the last,
     * middle and first digit of data register X's numerical value
     */
    private void executeFX33(short i) {
        byte register = dataRegisters.read((byte) ((i & 0x0F00) >>> 8));
        short memoryOffset = addressRegister.read();

        //example: 107
        memory.write(memoryOffset, (byte) (register % 10));                    // 7
        memory.write((short) (memoryOffset + 1), (byte) (register / 10 % 10)); // 0
        memory.write((short) (memoryOffset + 2), (byte) (register / 100));     // 1
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
    private short getNextInstruction() {
        short address = programCounter.read();

        short instruction = memory.read(address);
        instruction <<= 8;

        // & 0xFF removes sign bit
        instruction |= (short) (memory.read(++address) & 0xFF);

        programCounter.increment((short) 2);

        return instruction;
    }

    private void throwInstructionException(short instruction) {
        throw new UnsupportedOperationException(
                "CPU instruction " + Integer.toHexString(instruction & 0xFFFF)
        );
    }
}