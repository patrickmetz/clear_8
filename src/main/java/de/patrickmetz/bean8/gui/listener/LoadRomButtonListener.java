/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 19:52.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.listener;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.component.StatusPane;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadRomButtonListener implements ActionListener {

    private static final String FILE_EXTENSION = "ch8";
    private static final String FILE_EXTENSION_DESCRIPTION = "chip 8 ROMs (.ch8)";

    private final JComboBox<String> cpuComboBox;
    private final JButton pauseButton;
    private final Runner runner;
    private final StatusPane statusPane;
    private final JButton stopButton;

    public LoadRomButtonListener(Runner runner, StatusPane statusPane, JButton pauseButton,
                                 JButton stopButton,
                                 JComboBox<String> cpuComboBox) {
        this.runner = runner;
        this.statusPane = statusPane;

        this.pauseButton = pauseButton;
        this.stopButton = stopButton;
        this.cpuComboBox = cpuComboBox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = getFile(e);

        if (file != null) {

            if (!file.getPath().isBlank()) {
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                cpuComboBox.setEnabled(false);

                statusPane.setFileName(
                        file.getName().split("\\.")[0].toLowerCase()
                );

                if (runner.isRunning()) {
                    runner.stop();
                }

                runner.setRomPath(file.getPath());
                runner.run();
            }
        }
    }

    private File getFile(ActionEvent e) {
        File file = null;

        JFileChooser dialog = new JFileChooser();

        dialog.setFileFilter(
                new FileNameExtensionFilter(
                        FILE_EXTENSION_DESCRIPTION,
                        FILE_EXTENSION
                )
        );

        if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = dialog.getSelectedFile();
        }

        return file;
    }

}
