/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 23:43.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class ProgramCounter {

    private int value = 512;

    int read() {
        return value;
    }

    void skipInstruction() {
        this.value += 2;
    }

    void write(int value) {
        this.value = value;
    }

}