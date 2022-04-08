package de.patrickmetz.clear_8.emulator.hardware;

final class ProgramCounter {

    private int value;

    void increment(int numberOfBytes) {
        this.value += numberOfBytes;
    }

    int read() {
        return value;
    }

    void write(int value) {
        this.value = value;
    }

}