/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 19:06.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class DelayTimer {

    private int value = 60;

    public void decrement() {
        if (value > 0) {
            value--;
        }
    }

    public int read() {
        return value;
    }

    public void write(int value) {
        this.value = value;
    }
}