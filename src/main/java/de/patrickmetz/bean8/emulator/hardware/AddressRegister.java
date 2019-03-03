/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 13:09.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator.hardware;

final class AddressRegister {

    private int memory;

    int read() {
        return memory;
    }

    void write(int value) {
        memory = value;
    }

}