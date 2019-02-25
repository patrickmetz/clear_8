/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 25.02.19 18:37.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

final class Memory {

    static final short OFFSET_ROM = 512;

    // hash map read and write access surpasses array:
    // algorithmic complexity of get and put are at O(1)
    private final HashMap<Short, Byte> memory;

    Memory() {
        memory = new HashMap<>(4096, 2);
    }

    byte read(short address) {
        return memory.get(address);
    }

    byte[] read(short address, int count) {
        byte[] bytes = new byte[count];

        for (short b = 0, m = address; m < address + count; b++, m++) {
            bytes[b] = memory.get(m);
        }

        return bytes;
    }

    void write(Short address, byte value) {
        memory.put(address, value);
    }

}