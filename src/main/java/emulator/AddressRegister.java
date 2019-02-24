/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 20:55.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class AddressRegister {

    private int memory;

    int read() {
        return memory;
    }

    void write(int value) {
        memory = value;
    }

}