/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:23.
 * Copyright (c) 2019. All rights reserved.
 */

public class AddressRegister {

    private short memory;

    short read() {
        return memory;
    }

    void write(short value) {
        memory = value;
    }

}