/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 28.02.19 20:32.
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
     * @see CentralProcessingUnit#execute8XY6(short)
     */
    @Override
    protected void execute8XY6(short i) {
        dataRegisters.write(
                (byte) CARRY_FLAG,
                (byte) (dataRegisters.read((byte) ((i & EXPOSE_X) >> GET_X)) & 1)
        );

        dataRegisters.write(
                (byte) ((i & 0x0F00) >> 8),
                (byte) (dataRegisters.read((byte) ((i & EXPOSE_Y) >> GET_Y)) >> 1)
        );
    }

    /**
     * sets registers 0 to X to consecutive memory values
     * beginning at registered memory address
     * <p>
     * additionally increments address register by X
     *
     * @see CentralProcessingUnit#executeFX65(short)
     */
    @Override
    protected void executeFX65(short i) {
        super.executeFX65(i);

        addressRegister.write(
                (short) (
                        addressRegister.read()
                        + (byte) ((i & EXPOSE_X) >> GET_X)
                )
        );
    }
}
