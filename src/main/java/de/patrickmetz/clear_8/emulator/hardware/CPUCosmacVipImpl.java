package de.patrickmetz.clear_8.emulator.hardware;

import de.patrickmetz.clear_8.emulator.input.Keyboard;

/**
 * CPU implementation using opcodes of the Cosmac VIP
 * <p>
 * see: https://github.com/Chromatophore/HP48-Superchip#behavior-and-quirk-investigations
 */
class CPUCosmacVipImpl extends CPUSuperChipImpl {

    CPUCosmacVipImpl(AddressRegister addressRegister, CallStack callStack,
                     Registers dataRegisters, DelayTimer delayTimer,
                     Graphics graphics, Keyboard keyboard, Memory memory,
                     ProgramCounter programCounter, SoundTimer soundTimer) {

        super(addressRegister, callStack, dataRegisters,
                delayTimer, graphics, keyboard, memory,
                programCounter, soundTimer
        );
    }

    /**
     * Sets data register X to the value of data register Y, shifted to the right by one bit.
     * <p>
     * Stores the least significant bit of X's former value in the carry register.
     *
     * @see CPUSuperChipImpl#opcode8XY6(int)
     */
    @Override
    protected void opcode8XY6(int opcode) {
        registers.write(
                Registers.CARRY,
                registers.read(X(opcode)) & LSB
        );

        registers.write(
                X(opcode),
                registers.read(Y(opcode)) >> 1
        );
    }

    /**
     * Sets X consecutive memory values to the values of the data registers 0 to X, starting at the
     * registered memory address.
     * <p>
     * <p>
     * Additionally increments address register by X.
     *
     * @see CPUSuperChipImpl#opcodeFX55(int)
     */
    @Override
    protected void opcodeFX55(int opcode) {
        super.opcodeFX55(opcode);

        addressRegister.write(
                addressRegister.read()
                        + X(opcode)
        );
    }

    /**
     * Sets data registers 0 to X values to consecutive memory values beginning at the registered
     * memory address.
     * <p>
     * <p>
     * Additionally increments address register by X.
     *
     * @see CPUSuperChipImpl#opcodeFX65(int)
     */
    @Override
    protected void opcodeFX65(int opcode) {
        super.opcodeFX65(opcode);

        addressRegister.write(
                addressRegister.read()
                        + X(opcode)
        );
    }

}
