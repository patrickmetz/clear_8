/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 13:09.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator.hardware;

final class Graphics {

    private static final int SCREEN_HEIGHT = 32;
    private static final int SCREEN_WIDTH = 64;
    private static final int SPRITE_WIDTH = 8;

    private int[][] screenData;

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
                int newPixelState = (rows[y] >> x) & 1;

                int positionX = (offsetX + x) % SCREEN_WIDTH;
                int positionY = (offsetY + y) % SCREEN_HEIGHT;

                if ((newPixelState == 1) && (screenData[positionX][positionY] == 1)) {
                    newPixelState = 0;
                    collision = true;
                }

                screenData[positionX][positionY] = newPixelState;
            }
        }

        return collision;
    }

    int[][] getScreenData() {
        return screenData;
    }

    private void initializeScreenData() {
        screenData = new int[SCREEN_WIDTH][SCREEN_HEIGHT];
    }

}