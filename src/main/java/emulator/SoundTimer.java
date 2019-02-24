/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 23.02.19 17:16.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class SoundTimer {

    private final Thread sound;

    private byte value = 60;

    SoundTimer(Sound sound) {
        this.sound = new Thread(sound);
    }

    void decrement() {
        value--;

        checkValue();
    }

    void write(byte value) {
        this.value = value;

        checkValue();
    }

    private void checkValue() {
        if (value == 0) {
            sound.start();
        } else {
            sound.interrupt();
        }
    }
}