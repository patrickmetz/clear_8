/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 10.03.19 15:22.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator;

public interface Keyboard {

    boolean isKeyPressed(int keyCode);

    int waitForKey();

}
