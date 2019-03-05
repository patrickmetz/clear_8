/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 05.03.19 12:09.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator.hardware;

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