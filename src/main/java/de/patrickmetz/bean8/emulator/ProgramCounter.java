/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 02:35.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator;

final class ProgramCounter {

    private int value = 512;

    int read() {
        return value;
    }

    void skipOpcode() {
        this.value += 2;
    }

    void write(int value) {
        this.value = value;
    }

}