/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 17:29.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

final class Keyboard extends Thread {

    private final HashMap<Integer, Boolean> keys;

    Keyboard() {
        // 16 hexadecimal key codes, from 0 to F
        keys = new HashMap<>(16, 2);

        for (int i = 0; i < 0xF; i++) {
            keys.put(i, false);
        }
    }

    public void pressKey(int keyCode) {
        keys.put(keyCode, true);
    }

    public void releaseKey(int keyCode) {
        keys.put(keyCode, false);
    }

    boolean isKeyPressed(int keyCode) {
        return keys.get(keyCode);
    }

    Integer waitForKey() {
        return 0xF; // just fake it for now ;-P
    }

}