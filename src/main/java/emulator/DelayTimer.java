/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 02.03.19 00:16.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

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