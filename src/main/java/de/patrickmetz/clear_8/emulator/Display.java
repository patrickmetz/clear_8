package de.patrickmetz.clear_8.emulator;

public interface Display {

    void update(boolean[][] data);

    int getUpdateCount();
}
