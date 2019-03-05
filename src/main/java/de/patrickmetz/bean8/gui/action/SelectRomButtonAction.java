/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 05.03.19 17:43.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectRomButtonAction implements ActionListener {

    private static final String FILE_EXTENSION = "ch8";
    private static final String FILE_EXTENSION_DESCRIPTION = "chip 8 ROMs (.ch8)";

    private final JButton runButton;
    private final Runner runner;
    private final JTextPane statusPane;

    public SelectRomButtonAction(Runner runner, JTextPane statusPane, JButton runButton) {
        this.runner = runner;

        this.statusPane = statusPane;
        this.runButton = runButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String romPath = getRomPath(e);

        if (!romPath.isBlank()) {
            runner.setRomPath(romPath);

            runButton.setEnabled(true);
            statusPane.setText(romPath);
        }
    }

    private String getRomPath(ActionEvent e) {
        String romPath = "";

        JFileChooser dialog = new JFileChooser();

        dialog.setFileFilter(
                new FileNameExtensionFilter(
                        FILE_EXTENSION_DESCRIPTION,
                        FILE_EXTENSION
                )
        );

        if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            romPath = dialog.getSelectedFile().getPath();
        }

        return romPath;
    }
}
