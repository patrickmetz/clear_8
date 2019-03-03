/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 15:58.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui.action;

import de.patrickmetz.bean8.Runner;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectRomAction implements ActionListener {

    private final Runner runner;

    public SelectRomAction(Runner runner) {
        this.runner = runner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        runner.setRomPath(getRomPath(e));
    }

    private String getRomPath(ActionEvent e) {
        String romPath = "";

        JFileChooser dialog = new JFileChooser();
        int returnVal = dialog.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            romPath = dialog.getSelectedFile().getPath();
        }

        return romPath;
    }
}
