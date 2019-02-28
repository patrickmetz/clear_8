/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 27.02.19 00:38.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class ProgramCounter {

    private short value = 512;

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