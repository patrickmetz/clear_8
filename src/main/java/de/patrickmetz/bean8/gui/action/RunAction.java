/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 14:08.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunAction implements ActionListener {

    private final Runner runner;

    public RunAction(Runner runner) {
        this.runner = runner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        runner.run();
    }
}
