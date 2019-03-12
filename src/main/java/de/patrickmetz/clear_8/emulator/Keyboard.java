/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 13:59.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.emulator;

public interface Keyboard {

    int getNextKeyPressed();

    boolean isKeyPressed(int keyCode);

}
