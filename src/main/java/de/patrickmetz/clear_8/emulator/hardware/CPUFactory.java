package de.patrickmetz.clear_8.emulator.hardware;

import de.patrickmetz.clear_8.emulator.input.Keyboard;

final public class CPUFactory {

    public static CPU makeCpu(boolean useVipCpu, Keyboard keyboard) {
        if (useVipCpu) {
            return new CPUCosmacVipImpl(
                    new AddressRegister(), new CallStack(), new Registers(),
                    new DelayTimer(), new Graphics(), keyboard, new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound())
            );
        } else {
            return new CPUSuperChipImpl(
                    new AddressRegister(), new CallStack(), new Registers(),
                    new DelayTimer(), new Graphics(), keyboard, new Memory(),
                    new ProgramCounter(), new SoundTimer(new Sound())
            );
        }
    }

}
