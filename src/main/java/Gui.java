/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 02.03.19 21:16.
 * Copyright (c) 2019. All rights reserved.
 */

import javax.swing.*;

public class Gui {

    private JButton runButton;
    private Runner runner;
    private JButton selectROMButton;
    private JPanel window;

    Gui() {
        prepareSelectRomButton();
        prepareRunButton();
    }

    void render() {
        JPanel window = new Gui().window;
        renderWindow(window);
    }

    void setRunner(Runner runner) {
        this.runner = runner;
    }

    private void prepareRunButton() {
        runButton.addActionListener(e -> {
            System.err.println("run");
        });
    }

    private void prepareSelectRomButton() {
        selectROMButton.addActionListener(e -> {
            System.err.println("select");
        });
    }

    private void renderWindow(JPanel window) {
        JFrame frame = new JFrame("Gui");
        frame.setContentPane(window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
