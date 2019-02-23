/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

import javax.sound.sampled.LineUnavailableException;

class SoundTimer {

    private Sound sound;
    private byte value = 60;

    SoundTimer(Sound sound) {
        this.sound = sound;
    }

    void decrement() throws LineUnavailableException {
        value--;

        checkValue();
    }

    void write(byte value) throws LineUnavailableException {
        this.value = value;

        checkValue();
    }

    private void checkValue() throws LineUnavailableException {
        if (value == 0) {
            sound.play();
        }
    }
}