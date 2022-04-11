package de.patrickmetz.clear_8.emulator;

import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.input.Keyboard;
import de.patrickmetz.clear_8.gui.output.Display;

/**
 * The emulator is basically an endless loop. It continuously interprets
 * commands from a given game, while feeding the game keyboard input, and
 * displaying the game's graphical output on screen. It can be started,
 * stopped and paused and signals these state changes to interested listeners,
 * which react to those changes, like buttons in a graphical user interface.
 */
public interface Emulator {
    // in- and output ---------------------------------------------------------

    void setKeyboard(Keyboard keyboard);

    void setDisplay(Display display);

    // game options -----------------------------------------------------------

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

    // state change events ----------------------------------------------------

    void addStateListener(EmulatorEventListener listener);
}
