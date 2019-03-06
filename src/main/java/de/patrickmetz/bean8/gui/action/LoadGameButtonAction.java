/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 06.03.19 13:51.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadGameButtonAction implements ActionListener {

    private static final String FILE_EXTENSION = "ch8";
    private static final String FILE_EXTENSION_DESCRIPTION = "chip 8 ROMs (.ch8)";

    private final JButton pauseButton;
    private final Runner runner;
    private final JTextPane statusPane;

    public LoadGameButtonAction(Runner runner, JTextPane statusPane, JButton pauseButton) {
        this.runner = runner;

        this.statusPane = statusPane;
        this.pauseButton = pauseButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String romPath = getRomPath(e);

        if (romPath != null) {
            pauseButton.setEnabled(true);
            statusPane.setText(romPath);

            if (runner.isRunning()) {
                runner.stop();
            }

            runner.setRomPath(romPath);
            runner.run();
        }
    }

    private String getRomPath(ActionEvent e) {
        String romPath = null;

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
