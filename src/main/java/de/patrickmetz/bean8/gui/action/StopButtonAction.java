/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 11:06.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.Gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopButtonAction implements ActionListener {

    private final JComboBox<String> cpuComboBox;
    private final Timer fpsTimer;
    private final Gui gui;
    private final JButton pauseButton;
    private final Runner runner;

    public StopButtonAction(Runner runner, JButton pauseButton, Gui gui, Timer fpsTimer,
                            JComboBox<String> cpuComboBox) {
        this.runner = runner;
        this.pauseButton = pauseButton;
        this.gui = gui;
        this.fpsTimer = fpsTimer;
        this.cpuComboBox = cpuComboBox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton stopButton = (JButton) e.getSource();

        if (runner.isRunning()) {
            runner.stop();
            gui.resetDisplay();

            stopButton.setEnabled(false);
            pauseButton.setEnabled(false);
            cpuComboBox.setEnabled(true);

        }
    }
}
