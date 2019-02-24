/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 21:04.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

final class Memory {

    static final short OFFSET_ROM = 512;

    private final HashMap<Short, Byte> memory;

    Memory() {
        memory = new HashMap<>(4096, 2);
    }

    byte read(short address) {
        return memory.get(address);
    }

    void write(short address, byte value) {
        memory.put(address, value);
    }

}