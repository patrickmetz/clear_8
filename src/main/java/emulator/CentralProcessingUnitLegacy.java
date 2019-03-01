/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 17:35.
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
              programCounter, soundTimer);
    }

    /**
     * sets the value of data register X
     * to the value of data register Y,
     * shifted to the right by one bit.
     * <p>
     * the least significant bit of the
     * former value of X is stored in
     * the carry register
     * <p>
     *
     * @see CentralProcessingUnit#execute8XY6(int)
     */
    @Override
    protected void execute8XY6(int i) {
        dataRegisters.write(
                CARRY_FLAG,
                dataRegisters.read((i & EXPOSE_X) >> GET_X) & 1
        );

        dataRegisters.write(
                (i & EXPOSE_X) >> GET_X,
                dataRegisters.read((i & EXPOSE_Y) >> GET_Y) >> 1
        );
    }

    /**
     * sets registers 0 to X to consecutive memory values
     * beginning at registered memory address
     * <p>
     * additionally increments address register by X
     *
     * @see CentralProcessingUnit#executeFX65(int)
     */
    @Override
    protected void executeFX65(int i) {
        super.executeFX65(i);

        addressRegister.write(
                addressRegister.read()
                + ((i & EXPOSE_X) >> GET_X)
        );
    }
}
