/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 28.02.19 23:29.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

final class Keyboard extends Thread {

    private final HashMap<Byte, Boolean> keys;

    Keyboard() {
        // 16 hexadecimal key codes, from 0 to F
        keys = new HashMap<>(16, 2);

        for (int i = 0; i < 0xF; i++) {
            keys.put((byte) i, false);
        }
    }

    public void pressKey(byte keyCode) {
        keys.put(keyCode, true);
    }

    public void releaseKey(byte keyCode) {
        keys.put(keyCode, false);
    }

    boolean isKeyPressed(byte keyCode) {
        return keys.get(keyCode);
    }

    byte waitForKey() {
        return 0xF; // just fake it for now ;-P
    }

}