package de.patrickmetz.clear_8.emulator;

public interface Keyboard {

    int getNextKeyPressed();

    boolean isKeyPressed(int keyCode);

}
