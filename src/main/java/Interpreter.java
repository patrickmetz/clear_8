/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 21.02.19 21:56.
 * Copyright (c) 2019. All rights reserved.
 */

class Interpreter {

    private final int FRAMES_PER_SECOND = 60;
    private final int MILLISECONDS_PER_FRAME = 1000 / FRAMES_PER_SECOND;

    private int instructionsPerSecond;

    Interpreter(int instructionsPerSecond) {
        this.instructionsPerSecond = instructionsPerSecond;
    }

    void run() throws InterruptedException {
        final int INSTRUCTIONS_PER_FRAME = instructionsPerSecond / FRAMES_PER_SECOND;

        long now = System.currentTimeMillis();
        long endOfFrameTime;

        while (true) {
            endOfFrameTime = now + MILLISECONDS_PER_FRAME;

            for (int i = 0; i < INSTRUCTIONS_PER_FRAME; i++) {
                // cpu.processInstruction();
            }

            while (System.currentTimeMillis() < endOfFrameTime) {
                Thread.sleep(1);
            }

            now = System.currentTimeMillis();
            // display.renderFrame();
        }
    }
}
