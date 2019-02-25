/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 25.02.19 18:37.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class AddressRegister {

    private short memory;

    short read() {
        return memory;
    }

    void write(short value) {
        memory = value;
    }

}