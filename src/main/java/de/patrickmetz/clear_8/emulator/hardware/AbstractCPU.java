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
            memory.write(memoryAddress++, unsignedByte(d));
        }
    }

    @Override
    public void setProgramCounter(int memoryAdress) {
        programCounter.write(memoryAdress);
    }

    /**
     * Retrieves the rightmost hex digit (four bits) from an opcode.
     */
    protected static int N(int opcode) {
        return opcode & 0x000F;
    }

    /**
     * Retrieves the rightmost two hex digits (eight bits) from an opcode.
     */
    protected static int NN(int opcode) {
        return opcode & 0x00FF;
    }

    /**
     * Retrieves the rightmost three hex digits (twelve bits) from an opcode.
     */
    protected static int NNN(int opcode) {
        return opcode & 0x0FFF;
    }

    /**
     * Retrieves the X variable (bits 9 to 12) from an opcode.
     */
    protected static int X(int opcode) {
        return (opcode & 0x0F00) >> 8;
    }

    /**
     * Retrieves the Y variable (bits 5 to 8) from an opcode.
     */
    protected static int Y(int opcode) {
        return (opcode & 0x00F0) >> 4;
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

        opcode |= unsignedByte(memory.read(++address));

        programCounter.increment(OPCODE_SIZE);

        return opcode;
    }

    protected abstract void processNextInstruction() throws UnsupportedOperationException;

    protected void throwUnknownOpcodeException(int instruction) {
        throw new UnsupportedOperationException(
                "CPU instruction " + Integer.toHexString(instruction & 0xFFFF)
        );
    }

    /**
     * Returns a whole number, whose eight rightmost bits form
     * an unsigned byte.
     * <p>
     * Turns Javas signed byte values (-128 to 127) into chip8's
     * byte values (0 to 255).
     * <p>
     * Needed because the emulator stores chip8's bytes into integers.
     * <p>
     * <p>
     * Example:
     * int a = memory.read(someAddress);<p>
     * a is -32 or 11111111_11111111_11111111_11100000
     * <p>
     * <p>
     * int b = unsignedByte(a);<p>
     * b is 224 or 00000000_00000000_00000000_11100000
     * <p>
     * Putting the result into a java byte would add a sign again,
     * so we really need a bigger container, like int.
     * <p>
     * byte c = (byte) unsigned(a);<p>
     * c is -32 or 11100000
     */
    protected static int unsignedByte(int value) {
        return value & 0xFF;
    }

    private void updateTimers() {
        delayTimer.decrement();
        soundTimer.decrement();
    }
}
