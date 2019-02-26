/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 26.02.19 12:46.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class Graphics {

    private static final byte SCREEN_HEIGHT = 32;
    private static final byte SCREEN_WIDTH = 64;
    private static final byte SPRITE_WIDTH = 8;

    private byte[][] screen;

    Graphics() {
        initializeScreen();
    }

    void clearScreen() {
        initializeScreen();
    }

    boolean drawSprite(byte offsetX, byte offsetY, byte[] rows) {
        boolean collision = false;

        for (byte x = 0; x < SPRITE_WIDTH; x++) {
            for (byte y = 0; y < rows.length; y++) {
                byte newPixelState = (byte) ((rows[y] >> x) & 1);

                byte positionX = (byte) ((offsetX + x) % SCREEN_WIDTH);
                byte positionY = (byte) ((offsetY + y) % SCREEN_HEIGHT);

                if ((newPixelState == 1) && (screen[positionX][positionY] == 1)) {
                    newPixelState = 0;
                    collision = true;
                }

                screen[positionX][positionY] = newPixelState;
            }
        }

        return collision;
    }

    byte[][] getScreen() {
        return screen;
    }

    private void initializeScreen() {
        screen = new byte[SCREEN_WIDTH][SCREEN_HEIGHT];
    }

}