/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 13:01.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator;

class Font {

    /**
     * These are the fonts used by chip8;
     * spanning from 0 to F.
     *
     * Each character is made of five bytes,
     * so there is one character per row.
     *
     * Every byte represents one line of a character.
     * The lines are drawn from top to bottom.
     *
     * The 1-bits are drawn on screen, the 0-bits
     * are not. So the first line, for example, is
     * a bit depiction of the character 0:<p><p>
     *
     * 0xF0  =  1111<p>
     * 0x90  =  1001<p>
     * 0x90  =  1001<p>
     * 0xF0  =  1111<p>
     */
    private static final int[] BYTES = {
            0xF0, 0x90, 0x90, 0x90, 0xF0,
            0x20, 0x60, 0x20, 0x20, 0x70,
            0xF0, 0x10, 0xF0, 0x80, 0xF0,
            0xF0, 0x10, 0xF0, 0x10, 0xF0,
            0x90, 0x90, 0xF0, 0x10, 0x10,
            0xF0, 0x80, 0xF0, 0x10, 0xF0,
            0xF0, 0x80, 0xF0, 0x90, 0xF0,
            0xF0, 0x10, 0x20, 0x40, 0x50,
            0xF0, 0x90, 0xF0, 0x90, 0xF0,
            0xF0, 0x90, 0xF0, 0x10, 0xF0,
            0xF0, 0x90, 0xF0, 0x90, 0x90,
            0xE0, 0x90, 0xE0, 0x90, 0xE0,
            0xF0, 0x80, 0x80, 0x80, 0xF0,
            0xE0, 0x90, 0x90, 0x90, 0xE0,
            0xF0, 0x80, 0xF0, 0x80, 0xF0,
            0xF0, 0x80, 0xF0, 0x80, 0x80
    };

    static int[] getBytes() {
        return BYTES;
    }

}
