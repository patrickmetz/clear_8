package de.patrickmetz.clear_8.emulator.hardware;

import java.util.HashMap;

final class Memory {

    // hash map read and write access surpasses array:
    // algorithmic complexity of get() and put() is O(1)
    private final HashMap<Integer, Integer> memory;

    Memory() {
        memory = new HashMap<>(4096, 2);
    }

    int read(int address) {
        return memory.get(address);
    }

    int[] read(int address, int count) {
        int[] data = new int[count];

        for (int b = 0, m = address; m < address + count; b++, m++) {
            data[b] = memory.get(m);
        }

        return data;
    }

    void write(int address, int value) {
        memory.put(address, value);
    }

}