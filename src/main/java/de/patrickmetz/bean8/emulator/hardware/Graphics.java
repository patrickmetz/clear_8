/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 05.03.19 09:58.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator.hardware;

final class Graphics {

    private static final int SCREEN_HEIGHT = 32;
    private static final int SCREEN_WIDTH = 64;
    private static final int SPRITE_WIDTH = 8;

    private boolean[][] screenData;

    Graphics() {
        initializeScreenData();
    }

    void clearScreen() {
        initializeScreenData();
    }

    boolean drawSprite(int offsetX, int offsetY, int[] rows) {
        boolean collision = false;

        for (int x = 0; x < SPRITE_WIDTH; x++) {
            for (int y = 0; y < rows.length; y++) {
                // subsequently shifts each sprite bit, from index 7 to 0,
                // to the rightmost position, and grabs and compares it from there with 1
                boolean newPixelState =
                        ((rows[y] >> (SPRITE_WIDTH - x - 1)) & 1) == 1;


                int positionX = (offsetX + x) % SCREEN_WIDTH;
                int positionY = (offsetY + y) % SCREEN_HEIGHT;

                if ((newPixelState) && (screenData[positionX][positionY])) {
                    newPixelState = false;
                    collision = true;
                }

                screenData[positionX][positionY] = newPixelState;
            }
        }

        return collision;
    }

    boolean[][] getScreenData() {
        return screenData;
    }

    private void initializeScreenData() {
        screenData = new boolean[SCREEN_WIDTH][SCREEN_HEIGHT];
    }

}