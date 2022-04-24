package de.patrickmetz.clear_8.emulator.hardware;

/**
 * Stores unsigned 16-Bit values for addressing the memory space.
 * <p>
 * Uses the lower 16 Bits of a signed 32-Bit integer, because Java doesn't know
 * unsigned values.
 * <p>
 * Also, Chip-8 only uses the lower 12 bits of these 16, in order to address
 * 2^12 = 4096 Bits of memory.
 * <p>
 * In CHIP-8 terms this register was called "I".
 */
final class AddressRegister {

    private int memory;

    int read() {
        return memory;
    }

    void write(int value) {
        memory = value;
    }

}