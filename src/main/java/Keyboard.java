/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

import java.util.HashMap;

public class Keyboard {

    private HashMap<Byte, Boolean> keys;

    public Keyboard() {
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