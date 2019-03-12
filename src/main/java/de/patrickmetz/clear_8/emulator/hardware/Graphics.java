/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 13:59.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.emulator.hardware;

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

    boolean drawSprite(int offsetX, int offsetY, int[] spriteRows) {
        boolean collision = false;

        for (int x = 0; x < SPRITE_WIDTH; x++) {
            for (int y = 0; y < spriteRows.length; y++) {
                /* subsequently shifts each sprite bit, from index 7 to 0
                (i.e. 8th to 1st), to the rightmost position, grabs it from
                there and checks if it is equal to 1 (should be drawn) */
                boolean pixelState =
                        ((spriteRows[y] >> (SPRITE_WIDTH - x - 1)) & 0b00000001) == 1;

                /* puts pixels on their absolute screen position and "wraps
                them around", when they exceed screen width or height (e.g.
                width position 65 becomes 1, 66 becomes 2, 67 becomes 3, ...).
                So sprites exceeding the screen size re-enter it from the
                opposite side.
                 */
                int positionX = (offsetX + x) % SCREEN_WIDTH;
                int positionY = (offsetY + y) % SCREEN_HEIGHT;

                /* The pixels are dawn using XOR logic: They must exclusively
                be active in this draw call or in the last draw call, but not
                in both. If they're active in both, they won't be drawn and
                we register a collision. */
                if ((pixelState) && (screenData[positionX][positionY])) {
                    pixelState = false;
                    collision = true;
                }

                screenData[positionX][positionY] = pixelState;
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