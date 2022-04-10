package de.patrickmetz.clear_8.emulator;

import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.input.Keyboard;
import de.patrickmetz.clear_8.gui.output.Display;

public interface Emulator {
    // setters for in- and output ---------------------------------------------

    void setDisplay(Display display);

    void setKeyboard(Keyboard keyboard);

    // getters and setter for options -----------------------------------------

    String getGamePath();

    void setGamePath(String gamePath);

    int getInstructionsPerSecond();

    void setInstructionsPerSecond(int instructionsPerSecond);

    boolean getUseVipCpu();

    void setUseVipCpu(boolean useVipCpu);

    // state control ----------------------------------------------------------

    void start();

    void stop();

    void togglePause();

    // state events -----------------------------------------------------------

    void addStateListener(EmulatorEventListener listener);
}
