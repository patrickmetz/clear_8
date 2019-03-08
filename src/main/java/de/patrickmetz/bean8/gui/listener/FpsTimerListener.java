/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 19:36.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.gui.component.Display;
import de.patrickmetz.bean8.gui.component.StatusPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FpsTimerListener implements ActionListener {

    private final Display screen;
    private final StatusPane statusPane;

    private int updateCount = 0;

    public FpsTimerListener(Display screen, StatusPane statusPane) {
        this.screen = screen;
        this.statusPane = statusPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int newUpdateCount = screen.getUpdateCount();
        int fps = newUpdateCount - updateCount;
        updateCount = newUpdateCount;

        statusPane.setFps("" + fps);
    }

}
