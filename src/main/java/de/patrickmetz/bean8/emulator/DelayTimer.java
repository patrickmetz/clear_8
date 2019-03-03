/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 02:35.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator;

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