/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

public class Graphics {

    private boolean[][] pixels;

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