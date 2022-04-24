package de.patrickmetz.clear_8.emulator.hardware;

import java.util.HashMap;

/**
 * Contains 15 general purpose registers (0x0 to 0xE), and one carry flag
 * register (0xF) used by the CPU for doing its work.
 *
 * In CHIP-8 terms they were called "V0" to "VF".
 */
final class Registers {
    /**
     * The address of the carry register.
     * <p>
     * A carry is used to signal that a value overflows
     * its range, i.e. an unsigned byte bigger than 255
     * <p>
     * <p>
     * CHIP-8 also uses the carry to signal borrows
     * (value underflows) and when a pixel collision
     * is detected when drawing sprite graphics.
     */
    public static final int CARRY = 0xF;

    private final HashMap<Integer, Integer> memory;

    // Has a lookup and insertion performance of O(1).
    // A load factor of 2 should keep it from expanding at 75% capacity
    Registers() {
        memory = new HashMap<>(16, 2);
    }

    int read(int address) {
        return memory.get(address);
    }

    void write(int address, int value) {
        memory.put(address, value);
    }

}