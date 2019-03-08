/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 13:36.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.gui.component.Display;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FpsTimerAction implements ActionListener {

    private static final String text = "FPS: ";

    private final JTextPane fpsPane;
    private final Display screen;

    private int updateCount = 0;

    public FpsTimerAction(Display screen, JTextPane fpsPane) {
        this.screen = screen;
        this.fpsPane = fpsPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int newUpdateCount = screen.getUpdateCount();
        int fps = newUpdateCount - updateCount;
        updateCount = newUpdateCount;

        fpsPane.setText(text + fps);
    }
}
