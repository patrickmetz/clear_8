package de.patrickmetz.clear_8.emulator.hardware;

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