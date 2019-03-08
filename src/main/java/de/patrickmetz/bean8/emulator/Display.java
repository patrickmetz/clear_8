/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 11:11.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.emulator;

public interface Display {

    void update(boolean[][] data);

    int getUpdateCount();
}
