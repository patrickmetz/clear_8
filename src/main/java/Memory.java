/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

import java.util.HashMap;

public class Memory {

    private HashMap<Short, Byte> memory;

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