/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 21:04.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

public class DataRegisters {

    private HashMap<Byte, Byte> memory;

    public DataRegisters() {
        memory = new HashMap<>(16, 2);
    }

    byte read(byte address) {
        return memory.get(address);
    }

    void write(byte address, byte value) {
        memory.put(address, value);
    }

}