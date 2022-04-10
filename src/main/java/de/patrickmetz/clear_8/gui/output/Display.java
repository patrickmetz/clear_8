package de.patrickmetz.clear_8.gui.output;

public interface Display {

    void update(boolean[][] data);

    int getUpdateCount();
}
