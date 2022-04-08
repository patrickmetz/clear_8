package de.patrickmetz.clear_8.emulator.hardware;

final class DelayTimer {

    private int value = 60;

    void decrement() {
        if (value > 0) {
            value--;
        }
    }

    int read() {
        return value;
    }

    void write(int value) {
        this.value = value;
    }
}