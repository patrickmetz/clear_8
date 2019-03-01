/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 19:26.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class SoundTimer {

    private final Thread sound;

    private int value = 60;

    SoundTimer(Sound sound) {
        this.sound = new Thread(sound);
    }

    void decrement() {
        if (value > 0) {
            value--;
        }

        //checkValue();
    }

    void write(int value) {
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