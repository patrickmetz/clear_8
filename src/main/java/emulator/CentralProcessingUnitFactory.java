/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 28.02.19 14:05.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

class CentralProcessingUnitFactory {

    static CentralProcessingUnit makeCpu(boolean legacyMode) {
        if (legacyMode) {
            return new CentralProcessingUnitLegacy(
                    new AddressRegister(), new CallStack(), new DataRegisters(),
                    new DelayTimer(), new Graphics(), new Keyboard(), new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound()));
        } else {
            return new CentralProcessingUnit(
                    new AddressRegister(), new CallStack(), new DataRegisters(),
                    new DelayTimer(), new Graphics(), new Keyboard(), new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound()));
        }
    }
}
