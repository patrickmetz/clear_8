/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 20:10.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.Gui;
import de.patrickmetz.bean8.gui.component.output.StatusPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopButtonListener implements ActionListener {

    private final JComboBox<String> cpuComboBox;
    private final Gui gui;
    private final JButton pauseButton;
    private final Runner runner;
    private final StatusPane statusPane;

    public StopButtonListener(Runner runner, JButton pauseButton, Gui gui,
                              JComboBox<String> cpuComboBox,
                              StatusPane statusPane) {
        this.runner = runner;
        this.pauseButton = pauseButton;
        this.gui = gui;
        this.cpuComboBox = cpuComboBox;
        this.statusPane = statusPane;
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

            statusPane.setFps(null);
            statusPane.setFileName(null);
        }
    }
}
