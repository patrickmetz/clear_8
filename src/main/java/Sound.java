/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 */
class Sound {

    private static final boolean BIG_ENDIAN = false;
    private static final int CHANNELS = 1;
    private static final int HERTZ = 350;
    private static final int MILLISECONDS = 500;
    private static final float SAMPLE_RATE = 8000f;
    private static final int SAMPLE_SIZE_IN_BITS = 8;
    private static final boolean SIGNED = true;

    private static AudioFormat audioFormat;

    Sound() {
        audioFormat = new AudioFormat(
                SAMPLE_RATE,
                SAMPLE_SIZE_IN_BITS,
                CHANNELS,
                SIGNED,
                BIG_ENDIAN);
    }

    void play() throws LineUnavailableException {
        SourceDataLine sdl = AudioSystem.getSourceDataLine(audioFormat);

        byte[] buffer = new byte[1];

        sdl.open(audioFormat);
        sdl.start();

        for (int i = 0; i < MILLISECONDS * SAMPLE_SIZE_IN_BITS; i++) {
            buffer[0] = (byte) (sine(i) * 127.0);
            sdl.write(buffer, 0, buffer.length);
        }

        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    private double sine(int i) {
        return Math.sin(i / (SAMPLE_RATE / HERTZ) * 2.0 * Math.PI);
    }
}