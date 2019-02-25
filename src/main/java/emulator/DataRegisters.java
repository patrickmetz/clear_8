/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 25.02.19 18:37.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

final class DataRegisters {

    private final HashMap<Byte, Byte> memory;

    DataRegisters() {
        memory = new HashMap<>(16, 2);
    }

    byte read(byte address) {
        return memory.get(address);
    }

    void write(byte address, byte value) {
        memory.put(address, value);
    }

}