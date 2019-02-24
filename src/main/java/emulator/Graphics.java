/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 22:00.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class Graphics {

    private final boolean[][] pixels;

    Graphics() {
        pixels = new boolean[32][64];
    }

    boolean[][] getAllPixels() {
        return pixels;
    }

    boolean getPixel(byte x, byte y) {
        return pixels[x][y];
    }

    void setPixel(byte x, byte y, boolean value) {
        pixels[x][y] = value;
    }

}