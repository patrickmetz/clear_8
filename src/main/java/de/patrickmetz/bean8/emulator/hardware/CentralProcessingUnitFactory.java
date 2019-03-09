/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 17:17.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator.hardware;

public class CentralProcessingUnitFactory {

    public static CentralProcessingUnit makeCpu(boolean legacyMode) {
        if (legacyMode) {
            return new CentralProcessingUnitCosmacVip(
                    new AddressRegister(), new CallStack(), new DataRegisters(),
                    new DelayTimer(), new Graphics(), new Keyboard(), new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound())
            );
        } else {
            return new CentralProcessingUnit(
                    new AddressRegister(), new CallStack(), new DataRegisters(),
                    new DelayTimer(), new Graphics(), new Keyboard(), new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound())
            );
        }
    }

}
