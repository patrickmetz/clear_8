/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 07.03.19 20:31.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadRomButtonAction implements ActionListener {

    private static final String FILE_EXTENSION = "ch8";
    private static final String FILE_EXTENSION_DESCRIPTION = "chip 8 ROMs (.ch8)";

    private final JComboBox<String> cpuComboBox;
    private final JButton pauseButton;
    private final Runner runner;
    private final JTextPane statusPane;
    private final JButton stopButton;

    public LoadRomButtonAction(Runner runner, JTextPane statusPane, JButton pauseButton,
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
            String romPath = file.getPath();

            if (!romPath.isBlank()) {
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
                cpuComboBox.setEnabled(false);

                String name = file.getName();
                statusPane.setText(name);

                if (runner.isRunning()) {
                    runner.stop();
                }

                runner.setRomPath(romPath);
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
