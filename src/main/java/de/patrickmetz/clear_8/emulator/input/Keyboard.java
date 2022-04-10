package de.patrickmetz.clear_8.emulator.input;

public interface Keyboard {
    int getNextPressedKey();

    boolean isKeyPressed(int keyCode);
}
