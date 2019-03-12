/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 13:59.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.emulator.hardware;

import de.patrickmetz.clear_8.emulator.Keyboard;

public class CentralProcessingUnitFactory {

    public static CentralProcessingUnit makeCpu(boolean useVipCpu, Keyboard keyboard) {
        if (useVipCpu) {
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
