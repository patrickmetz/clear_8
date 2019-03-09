/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 15:53.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.runner.Runner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopButtonListener implements ActionListener {

    private final Runner runner;

    public StopButtonListener(Runner runner) {
        this.runner = runner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        runner.stop();
    }

}