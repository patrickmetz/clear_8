/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 15:40.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

final class Graphics {

    private static final int SCREEN_HEIGHT = 32;
    private static final int SCREEN_WIDTH = 64;
    private static final int SPRITE_WIDTH = 8;

    private int[][] screen;

    Graphics() {
        initializeScreen();
    }

    void clearScreen() {
        initializeScreen();
    }

    boolean drawSprite(int offsetX, int offsetY, int[] rows) {
        boolean collision = false;

        for (int x = 0; x < SPRITE_WIDTH; x++) {
            for (int y = 0; y < rows.length; y++) {
                int newPixelState = (rows[y] >> x) & 1;

                int positionX = (offsetX + x) % SCREEN_WIDTH;
                int positionY = (offsetY + y) % SCREEN_HEIGHT;

                if ((newPixelState == 1) && (screen[positionX][positionY] == 1)) {
                    newPixelState = 0;
                    collision = true;
                }

                screen[positionX][positionY] = newPixelState;
            }
        }

        return collision;
    }

    int[][] getScreen() {
        return screen;
    }

    private void initializeScreen() {
        screen = new int[SCREEN_WIDTH][SCREEN_HEIGHT];
    }

}