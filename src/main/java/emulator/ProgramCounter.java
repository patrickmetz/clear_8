/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 02.03.19 10:02.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

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