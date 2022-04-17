package de.patrickmetz.clear_8.emulator.hardware;

import de.patrickmetz.clear_8.emulator.input.Keyboard;

abstract public class AbstractCPU implements CPU {
    /**
     * The address of the carry register.
     * <p>
     * <p>
     * A carry is used to signal that a value overflows
     * its range, i.e. an unsigned byte bigger than 255
     * (which is than actually wrapped around with the
     * modulo operator, i.e. 256 becomes 0, 257 becomes
     * 1, etc...).
     * <p>
     * <p>
     * chip8 also uses the carry to signal borrows, i.e.
     * value underflows.
     */
    protected static final int CARRY = 0xF;
    /**
     * Bitmask to get the least significant (i.e. first) bit of a byte
     */
    protected static final int LSB   = 0b0000_0001;
    /**
     * Bitmask to get the most significant (i.e. last) bit of a byte
     */
    protected static final int MSB   = 0b1000_0000;

    /**
     * One opcode consists of two bytes
     */
    protected static final int OPCODE_SIZE = 2;

    /**
     * The maximum numerical value an unsigned byte can hold.
     */
    protected static final int UNSIGNED_BYTE_MAX_VALUE = 255;

    protected final AddressRegister addressRegister;
    protected final DataRegisters   dataRegisters;
    protected final CallStack       callStack;
    protected final DelayTimer      delayTimer;
    protected final Graphics        gpu;
    protected final Keyboard        keyboard;
    protected final Memory          memory;
    protected final ProgramCounter  programCounter;
    protected final SoundTimer      soundTimer;

    public AbstractCPU(AddressRegister addressRegister, DataRegisters dataRegisters, CallStack callStack, DelayTimer delayTimer, Graphics gpu, Keyboard keyboard, Memory memory, ProgramCounter programCounter, SoundTimer soundTimer) {
        this.addressRegister = addressRegister;
        this.dataRegisters = dataRegisters;
        this.callStack = callStack;
        this.delayTimer = delayTimer;
        this.gpu = gpu;
        this.keyboard = keyboard;
        this.memory = memory;
        this.programCounter = programCounter;
        this.soundTimer = soundTimer;
    }

    @Override
    public boolean[][] getDisplayData() {
        return gpu.getDisplayData();
    }

    @Override
    public void process(int instructionCount) {
        int i = 0;

        while (i < instructionCount) {
            processNextInstruction();
            i++;
        }

        updateTimers();
    }

    @Override
    public void setMemory(int[] data, int memoryAddress) {
        for (int d : data) {
            memory.write(memoryAddress++, unsigned(d));
        }
    }

    @Override
    public void setProgramCounter(int memoryAdress) {
        programCounter.write(memoryAdress);
    }

    /**
     * Constructs the next opcode to be executed.
     * <p>
     * <p>
     * Opcodes have a size of 16 bit. So we fetch
     * 2 bytes from memory to build them.
     * <p>
     * <p>
     * Example:
     * <p>
     * first byte from memory : 00000111
     * <p>
     * second byte from memory: 01010010
     * <p>
     * <p>
     * opcode               : 00000000|00000000<p>
     * opcode = first byte  : 00000000|00000111<p>
     * opcode <<= 8         : 00000111|00000000<p>
     * opcode |= second byte: 00000111|01010010
     */
    protected int getNextOpcode() {
        int address = programCounter.read();

        int opcode = memory.read(address);
        opcode <<= 8;

        opcode |= unsigned(memory.read(++address));

        programCounter.increment(OPCODE_SIZE);

        return opcode;
    }

    /**
     * Retrieves the rightmost hex digit (four bits) from an opcode.
     */
    protected static int n(int o) {
        return o & 0x000F;
    }

    /**
     * Retrieves the rightmost two hex digits (eight bits) from an opcode.
     */
    protected static int nn(int o) {
        return o & 0x00FF;
    }

    /**
     * Retrieves the rightmost three hex digits (twelve bits) from an opcode.
     */
    protected static int nnn(int o) {
        return o & 0x0FFF;
    }

    protected abstract void processNextInstruction() throws UnsupportedOperationException;

    protected void throwUnknownOpcodeException(int instruction) {
        throw new UnsupportedOperationException(
                "CPU instruction " + Integer.toHexString(instruction & 0xFFFF)
        );
    }

    /**
     * Retrieves the rightmost eight bits of a whole number,
     * in order to get a value from within an unsigned byte's
     * value range. Just make sure to put the result into a
     * whole number type which is bigger than a byte.
     * <p>
     * <p>
     * chip8 actually works with unsigned bytes (0 to 255),
     * but Java interprets those values as signed (-128 to 127).
     * <p>
     * <p>
     * When we need to correctly interpret the meaning of a whole
     * numbers bits (i.e. the value chip8 would assume) we use this
     * method.
     * <p>
     * <p>
     * This emulator stores the bytes of chip8 programs into
     * integers. So the following example is equivalent of reading
     * a byte from memory.
     * <p>
     * <p>
     * int a = memory.read(someAddress);<p>
     * a is -32 or 11111111_11111111_11111111_11100000
     * <p>
     * <p>
     * int b = unsigned(a);<p>
     * b is 224 or 00000000_00000000_00000000_11100000
     * <p>
     * <p>
     * but don't put the result in an actual byte,
     * or you'll get a signed value again:
     * <p>
     * byte c = (byte) unsigned(a);<p>
     * c = -32 or 11100000
     */
    protected static int unsigned(int value) {
        return value & 0xFF;
    }

    /**
     * Retrieves the X variable (bits 9 to 12) from an opcode.
     */
    protected static int x(int o) {
        return (o & 0x0F00) >> 8;
    }

    /**
     * Retrieves the Y variable (bits 5 to 8) from an opcode.
     */
    protected static int y(int o) {
        return (o & 0x00F0) >> 4;
    }

    private void updateTimers() {
        delayTimer.decrement();
        soundTimer.decrement();
    }
}
