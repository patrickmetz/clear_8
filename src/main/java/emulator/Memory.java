/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 21:04.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

final class Memory {

    static final short OFFSET_ROM = 512;

    // hash map read and write access surpasses array:
    // algorithmic complexity of get and put are at O(1)
    private final HashMap<Integer, Byte> memory;

    Memory() {
        memory = new HashMap<>(4096, 2);
    }

    byte read(int address) {
        return memory.get(address);
    }

    void write(int address, byte value) {
        memory.put(address, value);
    }

}