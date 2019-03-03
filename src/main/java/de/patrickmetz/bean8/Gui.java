/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 02:36.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import javax.swing.*;

public class Gui {

    private JButton runButton;
    private Runner runner;
    private JButton selectROMButton;
    private JPanel window;

    private Gui(Runner runner) {
        this.runner = runner;

        prepareSelectRomButton();
        prepareRunButton();
    }

    static void render(Runner runner) {
        JFrame frame = new JFrame("Gui");
        frame.setContentPane(new Gui(runner).window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private String getRomPath() {
        String romPath = "";

        JFileChooser dialog = new JFileChooser();
        int returnVal = dialog.showOpenDialog(window);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            romPath = dialog.getSelectedFile().getPath();
        }

        return romPath;
    }

    private void prepareRunButton() {
        runButton.addActionListener(
                e -> runner.run()
        );
    }

    private void prepareSelectRomButton() {
        selectROMButton.addActionListener(
                e -> runner.setRomPath(getRomPath())
        );
    }

}
