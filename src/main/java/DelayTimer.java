/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

public class DelayTimer {

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