/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 25.02.19 18:37.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.HashMap;

final class Keyboard {

    private final HashMap<Byte, Boolean> keys;

    Keyboard() {
        keys = new HashMap<>(16, 2);
    }

    public boolean getKeyState(byte keyCode) {
        return keys.get(keyCode);
    }

    public void pressKey(byte keyCode) {
        keys.put(keyCode, true);
    }

    public void releaseKey(byte keyCode) {
        keys.put(keyCode, false);
    }

    public void waitForKey(byte keyCode) {
        // TODO - implement Keyboard.waitForKey
        throw new UnsupportedOperationException();
    }

}