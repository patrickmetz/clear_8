/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 05.03.19 19:06.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.action.RunButtonAction;
import de.patrickmetz.bean8.gui.action.SelectRomButtonAction;

import javax.swing.*;
import java.awt.*;

public class Gui {

    private static Runner runner;

    private JPanel bottomPanel;
    private JPanel contentPane;
    private JPanel menuPanel;
    private JButton runButton;
    private Screen screen;
    private JPanel screenArea;
    private JButton selectRomButton;
    private JTextPane statusPane;

    private Gui() {
        setSelectRomButtonListener();
        setRunButtonListener();

        prepareStatusPane();
        prepareScreen();
        prepareRunButton();
    }

    /**
     * Creates the Graphical User Interface (GUI) and keeps
     * it responsive by putting it on Swings Event Dispatch
     * Thread (EDT).
     *
     * see: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
     */
    public static void render(Runner runner) {
        Gui.runner = runner;

        SwingUtilities.invokeLater(Gui::prepareGui);
    }

    private static JFrame createWindow(JPanel contentPane) {
        JFrame window = new JFrame("bean8");
        window.setContentPane(contentPane);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();

        return window;
    }

    private static void prepareGui() {
        Gui gui = new Gui();

        prepareWindow(
                createWindow(
                        gui.contentPane
                )
        );
    }

    private static void prepareWindow(JFrame window) {
        window.setLocationRelativeTo(null); // centers window
        window.setResizable(false);
        window.setVisible(true);
    }

    private void prepareRunButton() {
        if (!runner.getRomPath().isBlank()) {
            runButton.setEnabled(true);
            runButton.doClick();
        }
    }

    private void prepareScreen() {
        screenArea.setPreferredSize(new Dimension(640, 480));
        Screen screen = new Screen();

        screenArea.add(screen);
        runner.setScreen(screen);
    }

    private void prepareStatusPane() {
        String romPath = runner.getRomPath();

        if (!romPath.isBlank()) {
            statusPane.setText(romPath);
        }
    }

    private void setRunButtonListener() {
        runButton.addActionListener(
                new RunButtonAction(runner)
        );
    }

    private void setSelectRomButtonListener() {
        selectRomButton.addActionListener(
                new SelectRomButtonAction(runner, statusPane, runButton)
        );
    }

}
