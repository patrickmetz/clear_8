/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 25.02.19 18:37.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class Graphics {

    private static final byte SCREEN_HEIGHT = 32;
    private static final byte SCREEN_WIDTH = 64;
    private static final byte SPRITE_WIDTH = 8;

    private final byte[][] pixels;

    Graphics() {
        pixels = new byte[SCREEN_WIDTH][SCREEN_HEIGHT];
    }

    boolean drawSprite(byte offsetX, byte offsetY, byte[] rows) {
        boolean collision = false;

        for (byte x = 0; x < SPRITE_WIDTH; x++) {
            for (byte y = 0; y < rows.length; y++) {
                byte newPixelState = (byte) ((rows[y] >> x) & 1);

                byte positionX = (byte) ((offsetX + x) % SCREEN_WIDTH);
                byte positionY = (byte) ((offsetY + y) % SCREEN_HEIGHT);

                if ((newPixelState == 1) && (pixels[positionX][positionY] == 1)) {
                    newPixelState = 0;
                    collision = true;
                }

                pixels[positionX][positionY] = newPixelState;
            }
        }

        return collision;
    }

    byte[][] getAllPixels() {
        return pixels;
    }

}