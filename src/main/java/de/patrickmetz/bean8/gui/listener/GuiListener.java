/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 11.03.19 12:21.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.gui.Gui;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerState;

public class GuiListener implements RunnerEventListener {

    private final Gui gui;

    public GuiListener(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        if (e.getState() == RunnerState.STOPPED) {
            gui.resetDisplay();
        }
    }

}
