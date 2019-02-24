/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 23.02.19 02:17.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class DelayTimer {

    private byte value = 60;

    public void decrement() {
        value--;
    }

    public byte read() {
        return value;
    }

    public void write(byte value) {
        this.value = value;
    }
}