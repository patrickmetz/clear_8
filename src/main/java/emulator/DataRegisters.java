/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 15:23.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

final class DataRegisters {

    private final HashMap<Integer, Integer> memory;

    DataRegisters() {
        memory = new HashMap<>(16, 2);
    }

    Integer read(int address) {
        return memory.get(address);
    }

    void write(int address, int value) {
        memory.put(address, value);
    }

}