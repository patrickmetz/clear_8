package de.patrickmetz.clear_8.emulator.hardware;

/**
 * Contains the address of the next opcode the cpu should execute.
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