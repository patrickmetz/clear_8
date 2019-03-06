/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 06.03.19 20:59.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator;

public interface iScreen {

    void update(boolean[][] data);

    int getUpdateCount();
}
