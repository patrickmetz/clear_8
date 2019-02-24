/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 21:24.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class ProgramCounter {

    private int value = 0;

    void decrement(int value) {
        this.value -= value;
    }

    void decrement() {
        value--;
    }

    void increment(int value) {
        this.value += value;
    }

    void increment() {
        value++;
    }

    int read() {
        return value;
    }

    void write(int value) {
        this.value = value;
    }

}