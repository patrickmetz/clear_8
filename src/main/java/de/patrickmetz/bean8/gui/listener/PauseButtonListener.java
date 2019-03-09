/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 15:59.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.runner.Runner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseButtonListener implements ActionListener {

    private final Runner runner;

    public PauseButtonListener(Runner runner) {
        this.runner = runner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        runner.togglePause();
    }

}