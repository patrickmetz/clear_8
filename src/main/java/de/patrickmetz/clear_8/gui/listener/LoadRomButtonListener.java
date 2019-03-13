/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.gui.component.interaction.FileChooser;
import de.patrickmetz.clear_8.runner.Runner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

final public class LoadRomButtonListener implements ActionListener {

    private final FileChooser fileChooser;
    private final Runner runner;

    public LoadRomButtonListener(Runner runner, FileChooser fileChooser) {
        this.runner = runner;
        this.fileChooser = fileChooser;
    }

    /**
     * handles mouse clicks
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        File file = fileChooser.getFile();

        if (file != null) {
            runner.stop();
            runner.setRomPath(file.getPath());
            runner.start();
        }
    }

}
