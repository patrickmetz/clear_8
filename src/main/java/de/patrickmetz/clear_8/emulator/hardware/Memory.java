package de.patrickmetz.clear_8.emulator.hardware;

import java.util.HashMap;

/**
 * Contains 2^12 = 4096 addressable memory cells which form CHIP-8's working memory.
 *
 *  Signed 32-bit integers act as a container for CHIP-8's unsigned 8-bit memory cells.
 */
final class Memory {

    private final HashMap<Integer, Integer> memory;

    // Has a lookup and insertion performance of O(1).
    // A load factor of 2 should keep it from expanding at 75% capacity
    Memory() {
        memory = new HashMap<>(4096, 2);
    }

    int read(int address) {
        return memory.get(address);
    }

    int[] read(int address, int count) {
        int[] data = new int[count];

        for (
                int dataOffset = 0, memoryOffset = address;
                memoryOffset < address + count;
                dataOffset++, memoryOffset++
        ) {
            data[dataOffset] = memory.get(memoryOffset);
        }

        return data;
    }

    void write(int address, int value) {
        memory.put(address, value);
    }

}