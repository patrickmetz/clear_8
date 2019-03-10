/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 10.03.19 18:46.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator.hardware;

import de.patrickmetz.bean8.emulator.Keyboard;

public class CentralProcessingUnitFactory {

    public static CentralProcessingUnit makeCpu(boolean legacyMode, Keyboard keyboard) {
        if (legacyMode) {
            return new CentralProcessingUnitCosmacVip(
                    new AddressRegister(), new CallStack(), new DataRegisters(),
                    new DelayTimer(), new Graphics(), keyboard, new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound())
            );
        } else {
            return new CentralProcessingUnit(
                    new AddressRegister(), new CallStack(), new DataRegisters(),
                    new DelayTimer(), new Graphics(), keyboard, new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound())
            );
        }
    }

}
