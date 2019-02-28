/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 26.02.19 21:06.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

/**
 * see:
 * https://github.com/JohnEarnest/Octo/blob/39e001152f55b2ae44dadb8d23fd27dfc415fed1/docs/SuperChip.md
 * https://github.com/Chromatophore/HP48-Superchip
 * https://www.reddit.com/r/EmuDev/comments/8cbvz6/chip8_8xy6/
 * https://www.reddit.com/r/EmuDev/comments/72dunw/chip8_8xy6_help/
 * http://laurencescotford.co.uk/?p=266
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
     * data register F
     * <p>
     *
     * @see CentralProcessingUnit#execute8XY6(short)
     */
    @Override
    protected void execute8XY6(short i) {
        dataRegisters.write(
                (byte) 0xF,
                (byte) (dataRegisters.read((byte) ((i & 0x0F00) >> 8)) & 1)
        );

        dataRegisters.write(
                (byte) ((i & 0x0F00) >> 8),
                (byte) (dataRegisters.read((byte) ((i & 0x00F0) >> 4)) >> 1)
        );
    }

}
