package de.patrickmetz.clear_8.emulator;

import de.patrickmetz.clear_8.emulator.event.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.input.Keyboard;
import de.patrickmetz.clear_8.gui.component.output.Display;

public interface Emulator {
    // state control ----------------------------------------------------------

    void start();

    void stop();

    void togglePause();

    // state events -----------------------------------------------------------

    void addStateListener(EmulatorEventListener listener);

    // getters and setter for options -----------------------------------------

    String getGamePath();

    void setGamePath(String gamePath);

    int getInstructionsPerSecond();

    void setInstructionsPerSecond(int instructionsPerSecond);

    boolean getUseVipCpu();

    void setUseVipCpu(boolean useVipCpu);

    void setDisplay(Display display);

    void setKeyboard(Keyboard keyboard);
}
