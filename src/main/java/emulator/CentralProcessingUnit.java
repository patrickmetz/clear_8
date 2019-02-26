/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 26.02.19 12:34.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class CentralProcessingUnit {

    private final AddressRegister addressRegister;
    private final CallStack callStack;
    private final DataRegisters dataRegisters;
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
    void executeNextInstruction() throws UnsupportedOperationException {
        short instruction = getNextInstruction();

        switch (instruction & 0xF000) {
            case 0x1000:
                execute1NNN(instruction);
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
            case 0xA000:
                executeANNN(instruction);
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
                    default:
                        throwInstructionException(instruction);
                }
                break;
            default:
                throwInstructionException(instruction);
                return;
        }

    }

    void loadRomIntoMemory(byte[] bytes) {
        short offset = Memory.OFFSET_ROM;

        for (byte b : bytes) {
            memory.write(offset++, b);
        }

        programCounter.write(Memory.OFFSET_ROM);
    }

    /**
     * sets program counter to value NNN
     */
    private void execute1NNN(short i) {
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
            // one instruction = two bytes
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
        byte registerAddress = (byte) ((i & 0x0F00) >> 8);
        int newValue = dataRegisters.read(registerAddress) + (i & 0x00FF);

        if (newValue > 255) {
            newValue %= 255;
        }

        dataRegisters.write(registerAddress, (byte) newValue);
    }

    /**
     * sets address register to value NNN
     */
    private void executeANNN(short i) {
        addressRegister.write((short) (i & 0x0FFF));
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

        /*
            (short)(byte & 0xFF) converts signed to unsigned:

            It takes all the bits of the byte with a bitwise and
            (0xFF is 1111_1111), puts them into a bigger short,
            so that Java "forgets" the Two`s complement, e.g.
            the most significant bit of the negative value gets lost.

            So the following works:
            byte1: 00000000 (dec 0, hex 0x0)
            byte2: 11100000 (dec -32, hex 0xE0)
            instruction : 00000000_11100000 (dec 224, hex 0xE0)
            => result matches opcode 00E0 (clear screen)

            But, if we didn't do that, and used the byte directly:
            byte1: 00000000 (dec 0, hex 0x0)
            byte2: 11100000 (dec -32, hex 0xE0)
            instruction: 11111111_11100000 (dec -32, hex 0xFFE0)
            => result FFE0 is just garbage
         */
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