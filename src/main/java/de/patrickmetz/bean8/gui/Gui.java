/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 05.03.19 17:20.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.action.RunButtonAction;
import de.patrickmetz.bean8.gui.action.SelectRomButtonAction;

import javax.swing.*;
import java.awt.*;

public class Gui {

    private JPanel bottomPanel;
    private JPanel contentPane;
    private JPanel menuPanel;
    private JButton runButton;
    private JPanel screenArea;
    private JButton selectRomButton;
    private JTextPane statusPane;

    private Gui(Runner runner) {
        setSelectRomButtonListener(runner, this);
        setRunButtonListener(runner);

        prepareStatusPane(runner);
        prepareScreen(runner);
        prepareRunButton(runner);
    }

    /**
     * Creates the Graphical User Interface (GUI) and keeps
     * it responsive by putting it on Swings Event Dispatch
     * Thread (EDT).
     *
     * see: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
     */
    public static void render(Runner runner) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                prepareGui(runner);
            }
        });
    }

    private static JFrame createWindow(Runner runner) {
        JFrame window = new JFrame("bean8");
        window.setContentPane(new Gui(runner).contentPane);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();

        return window;
    }

    private static void prepareGui(Runner runner) {
        prepareWindow(createWindow(runner));
    }

    private static void prepareWindow(JFrame window) {
        window.setLocationRelativeTo(null); // centers window
        window.setResizable(false);
        window.setVisible(true);
    }

    private void prepareRunButton(Runner runner) {
        if (!runner.getRomPath().isBlank()) {
            runButton.setEnabled(true);
            runButton.doClick();
        }
    }

    private void prepareScreen(Runner runner) {
        screenArea.setPreferredSize(new Dimension(640, 480));
        Screen screen = new Screen();

        screenArea.add(screen);
        runner.setScreen(screen);
    }

    private void prepareStatusPane(Runner runner) {
        if (!runner.getRomPath().isBlank()) {
            statusPane.setText(runner.getRomPath());
        }
    }

    private void setRunButtonListener(Runner runner) {
        runButton.addActionListener(
                new RunButtonAction(runner)
        );
    }

    private void setSelectRomButtonListener(Runner runner, Gui gui) {
        selectRomButton.addActionListener(
                new SelectRomButtonAction(runner, statusPane, runButton)
        );
    }

}
