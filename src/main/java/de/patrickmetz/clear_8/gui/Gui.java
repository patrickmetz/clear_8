/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.gui;

import de.patrickmetz.clear_8.gui.component.interaction.*;
import de.patrickmetz.clear_8.gui.component.output.Display;
import de.patrickmetz.clear_8.gui.component.output.StatusPane;
import de.patrickmetz.clear_8.gui.component.structure.Window;
import de.patrickmetz.clear_8.gui.component.structure.*;
import de.patrickmetz.clear_8.gui.listener.*;
import de.patrickmetz.clear_8.gui.timer.FpsTimer;
import de.patrickmetz.clear_8.runner.Runner;

import javax.swing.*;
import java.awt.*;

final public class Gui {

    private static boolean shown;
    private static Runner runner;
    private static Window window;

    // window structure

    private WindowContent windowContent;

    private TopPanel topPanel;
    private CenterPanel centerPanel;
    private BottomPanel bottomPanel;

    // interactive elements

    private LoadRomButton loadRomButton;
    private PauseButton pauseButton;
    private StopButton stopButton;

    private FileChooser fileChooser;

    private CpuComboBox cpuComboBox;
    private InstructionsComboBox instructionsComboBox;

    // output elements

    private Display display;
    private FpsTimer fpsTimer;
    private StatusPane statusPane;

    private Gui() {
        constructComponents();
        initializeComponents();

        constructFpsTimer();
        setListenersUp();

        runner.setDisplay(display);
    }

    // see: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
    public static void show(Runner runner) {
        if (shown) {
            return;
        }
        shown = true;

        Gui.runner = runner;

        SwingUtilities.invokeLater(Gui::createGui);

        if (runner.getRomPath() != null) {
            SwingUtilities.invokeLater(runner::start);
        }
    }

    public void resetDisplay() {
        centerPanel.remove(display);

        display = new Display();
        centerPanel.add(display);

        runner.setDisplay(display);
        fpsTimer.setDisplay(display);
    }

    private static void createGui() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        window = new Window();
        window.setContentPane(new Gui().windowContent);
        window.pack();
        window.setLocationRelativeTo(null); // centers window
        window.setVisible(true);
    }

    private void constructComponents() {
        // window structure

        windowContent = new WindowContent();

        topPanel = new TopPanel();
        windowContent.add(topPanel, BorderLayout.NORTH);

        centerPanel = new CenterPanel();
        windowContent.add(centerPanel, BorderLayout.CENTER);

        bottomPanel = new BottomPanel();
        windowContent.add(bottomPanel, BorderLayout.SOUTH);

        // interactive elements

        fileChooser = new FileChooser();

        loadRomButton = new LoadRomButton();
        topPanel.add(loadRomButton);

        pauseButton = new PauseButton();
        topPanel.add(pauseButton);

        stopButton = new StopButton();
        topPanel.add(stopButton);

        cpuComboBox = new CpuComboBox();
        topPanel.add(cpuComboBox);

        instructionsComboBox = new InstructionsComboBox();
        topPanel.add(instructionsComboBox);

        // output elements

        display = new Display();
        centerPanel.add(display);

        statusPane = new StatusPane();
        bottomPanel.add(statusPane);
    }

    private void constructFpsTimer() {
        fpsTimer = new FpsTimer(display, statusPane);
    }

    private void initializeComponents() {
        cpuComboBox.setSelectedItem(
                runner.getUseVipCpu()
        );

        instructionsComboBox.setSelectedItem(
                runner.getInstructionsPerSecond()
        );
    }

    private void setListenersUp() {

        // create mouse and runner listeners

        LoadRomButtonListener loadRomButtonListener =
                new LoadRomButtonListener(runner, fileChooser);

        PauseButtonListener pauseButtonListener =
                new PauseButtonListener(runner, pauseButton);

        StopButtonListener stopButtonListener =
                new StopButtonListener(runner, stopButton);

        CpuComboBoxListener cpuComboBoxListener =
                new CpuComboBoxListener(runner, cpuComboBox);

        InstructionsComboBoxListener instructionsComboBoxListener =
                new InstructionsComboBoxListener(runner, instructionsComboBox);

        StatusPaneListener statusPaneListener =
                new StatusPaneListener(statusPane);

        GuiListener guiListener = new GuiListener(this);

        // connect mouse click listeners

        loadRomButton.addActionListener(loadRomButtonListener);
        pauseButton.addActionListener(pauseButtonListener);
        stopButton.addActionListener(stopButtonListener);
        cpuComboBox.addItemListener(cpuComboBoxListener);
        instructionsComboBox.addItemListener(instructionsComboBoxListener);

        // connect runner state change listeners

        runner.addListener(pauseButtonListener);
        runner.addListener(stopButtonListener);
        runner.addListener(cpuComboBoxListener);
        runner.addListener(instructionsComboBoxListener);

        runner.addListener(statusPaneListener);
        runner.addListener(fpsTimer);

        runner.addListener(guiListener);

        // create and connect keyboard listener

        KeyboardListener keyboardListener = new KeyboardListener();
        window.addKeyListener(keyboardListener);
        runner.setKeyboard(keyboardListener);
    }

}
