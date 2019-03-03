/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 13:09.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator.hardware;

import java.util.HashMap;

final class DataRegisters {

    private final HashMap<Integer, Integer> memory;

    DataRegisters() {
        memory = new HashMap<>(16, 2);
    }

    int read(int address) {
        return memory.get(address);
    }

    void write(int address, int value) {
        memory.put(address, value);
    }

}