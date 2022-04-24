package de.patrickmetz.clear_8.emulator.hardware;

/**
 * Contains the memory address of the next 16-bit wide cpu command to be executed.
 * A signed 32-bit acts as a container for CHIP-8's unsigned 16 bit addresses.
 */
final class ProgramCounter {

    /**
     * One opcode consists of two bytes
     */
    private static final int OPCODE_SIZE = 2;

    private int opcodeAddress;

    void increment() {
        this.opcodeAddress += OPCODE_SIZE;
    }

    int read() {
        return opcodeAddress;
    }

    void write(int value) {
        this.opcodeAddress = value;
    }

}