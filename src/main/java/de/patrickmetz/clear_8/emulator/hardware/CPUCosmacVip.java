package de.patrickmetz.clear_8.emulator.hardware;

import de.patrickmetz.clear_8.emulator.input.Keyboard;

/**
 * CPU implementation using opcodes of the Cosmac VIP
 * <p>
 * see: https://github.com/Chromatophore/HP48-Superchip#behavior-and-quirk-investigations
 */
class CPUCosmacVip extends CPUSuperChip {

    CPUCosmacVip(AddressRegister addressRegister, CallStack callStack,
                 DataRegisters dataRegisters, DelayTimer delayTimer,
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
     * @see CPUSuperChip#opcode8XY6(int)
     */
    @Override
    protected void opcode8XY6(int o) {
        dataRegisters.write(
                CARRY,
                dataRegisters.read(x(o)) & LSB
        );

        dataRegisters.write(
                x(o),
                dataRegisters.read(y(o)) >> 1
        );
    }

    /**
     * Sets X consecutive memory values to the values of the data registers 0 to X, starting at the
     * registered memory address.
     * <p>
     * <p>
     * Additionally increments address register by X.
     *
     * @see CPUSuperChip#opcodeFX55(int)
     */
    @Override
    protected void opcodeFX55(int o) {
        super.opcodeFX55(o);

        addressRegister.write(
                addressRegister.read()
                + x(o)
        );
    }

    /**
     * Sets data registers 0 to X values to consecutive memory values beginning at the registered
     * memory address.
     * <p>
     * <p>
     * Additionally increments address register by X.
     *
     * @see CPUSuperChip#opcodeFX65(int)
     */
    @Override
    protected void opcodeFX65(int o) {
        super.opcodeFX65(o);

        addressRegister.write(
                addressRegister.read()
                + x(o)
        );
    }

}
