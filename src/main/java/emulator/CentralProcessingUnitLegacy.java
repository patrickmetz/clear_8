/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 02.03.19 15:19.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

/**
 * CPU implementation using opcodes of the Cosmac VIP
 * <p>
 * see:
 * http://www.mattmik.com/files/chip8/mastering/chip8.html
 * http://devernay.free.fr/hacks/chip8/C8TECH10.HTM
 * https://github.com/Chromatophore/HP48-Superchip#behavior-and-quirk-investigations
 */
public class CentralProcessingUnitLegacy extends CentralProcessingUnit {

    CentralProcessingUnitLegacy(AddressRegister addressRegister, CallStack callStack,
                                DataRegisters dataRegisters, DelayTimer delayTimer,
                                Graphics graphics, Keyboard keyboard, Memory memory,
                                ProgramCounter programCounter, SoundTimer soundTimer) {

        super(addressRegister, callStack, dataRegisters,
              delayTimer, graphics, keyboard, memory,
              programCounter, soundTimer
        );
    }

    /**
     * Sets data register X to the value of data register Y,
     * shifted to the right by one bit.
     * <p>
     * Stores the least significant bit of X's former value
     * in the carry register.
     *
     * @see CentralProcessingUnit#opcode8XY6(int)
     */
    @Override
    protected void opcode8XY6(int o) {
        dataRegisters.write(
                CARRY,
                dataRegisters.read(X(o)) & LEAST_SIGNIFICANT_BIT
        );

        dataRegisters.write(
                X(o),
                dataRegisters.read(Y(o)) >> 1
        );
    }

    /**
     * Sets X consecutive memory values to the values of the data
     * registers 0 to X, starting at the registered memory address.
     * <p>
     * <p>
     * Additionally increments address register by X.
     *
     * @see CentralProcessingUnit#opcodeFX55(int)
     */
    @Override
    protected void opcodeFX55(int o) {
        super.opcodeFX55(o);

        addressRegister.write(
                addressRegister.read()
                + X(o)
        );
    }

    /**
     * Sets data registers 0 to X values to consecutive memory
     * values beginning at the registered memory address.
     * <p>
     * <p>
     * Additionally increments address register by X.
     *
     * @see CentralProcessingUnit#opcodeFX65(int)
     */
    @Override
    protected void opcodeFX65(int o) {
        super.opcodeFX65(o);

        addressRegister.write(
                addressRegister.read()
                + X(o)
        );
    }
}
