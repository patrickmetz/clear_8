/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 10.03.19 18:46.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator.hardware;

import de.patrickmetz.bean8.emulator.Keyboard;

/**
 * CPU implementation using opcodes of the Cosmac VIP
 * <p>
 * see: https://github.com/Chromatophore/HP48-Superchip#behavior-and-quirk-investigations
 */
public class CentralProcessingUnitCosmacVip extends CentralProcessingUnit {

    CentralProcessingUnitCosmacVip(AddressRegister addressRegister, CallStack callStack,
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
     * @see CentralProcessingUnit#opcode8XY6(int)
     */
    @Override
    protected void opcode8XY6(int o) {
        dataRegisters.write(
                CARRY,
                dataRegisters.read(X(o)) & LSB
        );

        dataRegisters.write(
                X(o),
                dataRegisters.read(Y(o)) >> 1
        );
    }

    /**
     * Sets X consecutive memory values to the values of the data registers 0 to X, starting at the
     * registered memory address.
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
     * Sets data registers 0 to X values to consecutive memory values beginning at the registered
     * memory address.
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
