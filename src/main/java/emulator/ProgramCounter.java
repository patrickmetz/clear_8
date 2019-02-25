/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 21:24.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class ProgramCounter {

    private short value = 0;

    void decrement(short value) {
        this.value -= value;
    }

    void decrement() {
        value--;
    }

    void increment(short value) {
        this.value += value;
    }

    void increment() {
        value++;
    }

    short read() {
        return value;
    }

    void write(short value) {
        this.value = value;
    }

}