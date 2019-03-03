/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 13:09.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator.hardware;

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