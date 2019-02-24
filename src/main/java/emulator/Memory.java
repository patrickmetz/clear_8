/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 21:04.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

public class Memory {

    private HashMap<Short, Byte> memory;

    public Memory() {
        memory = new HashMap<>(4096, 2);
    }

    byte read(short address) {
        return memory.get(address);
    }

    void write(short address, byte value) {
        memory.put(address, value);
    }

}