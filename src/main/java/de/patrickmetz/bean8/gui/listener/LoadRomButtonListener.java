/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 20:58.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.component.interaction.FileDialog;
import de.patrickmetz.bean8.gui.component.output.StatusPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadRomButtonListener implements ActionListener {

    private final JComboBox<String> cpuComboBox;
    private final FileDialog fileDialog;
    private final JButton pauseButton;
    private final Runner runner;
    private final StatusPane statusPane;
    private final JButton stopButton;

    public LoadRomButtonListener(Runner runner, StatusPane statusPane, JButton pauseButton,
                                 JButton stopButton,
                                 JComboBox<String> cpuComboBox, FileDialog fileDialog) {
        this.runner = runner;
        this.statusPane = statusPane;

        this.pauseButton = pauseButton;
        this.stopButton = stopButton;
        this.cpuComboBox = cpuComboBox;
        this.fileDialog = fileDialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = fileDialog.getFile();

        if (file != null) {

            if (!file.getPath().isBlank()) {
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                cpuComboBox.setEnabled(false);

                statusPane.setFileName(
                        file.getName()
                );

                if (runner.isRunning()) {
                    runner.stop();
                }

                runner.setRomPath(file.getPath());
                runner.run();
            }
        }
    }

}
