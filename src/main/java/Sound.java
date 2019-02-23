/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

class Sound {

    private static final float SAMPLE_RATE_IN_HZ = 1000f;
    private static final int TONE_DURATION_IN_MS = 1000;
    private static final int TONE_FREQUENCY_IN_HZ = 400;
    private static final int TONE_VOLUME = 100;

    private static AudioFormat audioFormat;

    Sound() {
        audioFormat = new AudioFormat(
                SAMPLE_RATE_IN_HZ,
                8,
                1,
                true,
                false);
    }

    void play() throws LineUnavailableException {
        SourceDataLine sdl = AudioSystem.getSourceDataLine(audioFormat);

        byte[] buffer = new byte[1];

        sdl.open(audioFormat);
        sdl.start();

        for (int i = 0; i < TONE_DURATION_IN_MS * SAMPLE_RATE_IN_HZ / 1000; i++) {
            buffer[0] = (byte) (sine(i));
            sdl.write(buffer, 0, buffer.length);
        }

        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    private double sine(double i) {
        return Math.sin(
                i / (SAMPLE_RATE_IN_HZ / TONE_FREQUENCY_IN_HZ) * 2.0 * Math.PI) * TONE_VOLUME;
    }
}