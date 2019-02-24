/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 21:24.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class ProgramCounter {

    private short value = 0;

    void decrement(short value) {
        value -= value;
    }

    void increment(short value) {
        value += value;
    }

    void set(short value) {
        this.value = value;
    }

}