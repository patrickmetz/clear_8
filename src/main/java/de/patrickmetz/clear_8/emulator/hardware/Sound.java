package de.patrickmetz.clear_8.emulator.hardware;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

final class Sound extends Thread {

    private static final float SAMPLE_RATE_IN_HZ = 8000f;
    private static final int TONE_FREQUENCY_IN_HZ = 400;
    private static final float SAMPLE_HZ_PER_TONE_HZ = SAMPLE_RATE_IN_HZ / TONE_FREQUENCY_IN_HZ;
    private static final int TONE_VOLUME = 100;

    private SourceDataLine sdl;

    Sound() {
        AudioFormat audioFormat = new AudioFormat(
                SAMPLE_RATE_IN_HZ,
                8,
                1,
                true,
                false
        );

        try {
            sdl = AudioSystem.getSourceDataLine(audioFormat);
        } catch (LineUnavailableException e) {
            //todo: log this
            e.printStackTrace();
        }

        try {
            sdl.open(audioFormat);
        } catch (LineUnavailableException e) {
            //todo: log this
            e.printStackTrace();
        }
    }

    public void run() {
        byte[] output = new byte[1];
        int outputBuffer;
        int outputWritten = 0;

        sdl.start();

        while (!Thread.currentThread().isInterrupted()) {
            outputBuffer = sdl.available();

            // don't exceed buffer size to keep the thread interruptible
            while (outputWritten < outputBuffer) {
                output[0] = (byte) sineWave(outputWritten++);
                sdl.write(output, 0, 1);
            }

            outputWritten = 0;
        }

        sdl.stop();
        sdl.close();
    }

    private double sineWave(double sampleIndex) {
        // sin(x * 2.0 * PI) * amplitude
        return Math.sin(sampleIndex / SAMPLE_HZ_PER_TONE_HZ * 2.0 * Math.PI) * TONE_VOLUME;
    }
}