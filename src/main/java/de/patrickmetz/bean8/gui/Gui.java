/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 17:10.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.gui.component.interaction.*;
import de.patrickmetz.bean8.gui.component.output.Display;
import de.patrickmetz.bean8.gui.component.output.StatusPane;
import de.patrickmetz.bean8.gui.component.structure.Window;
import de.patrickmetz.bean8.gui.component.structure.*;
import de.patrickmetz.bean8.gui.listener.CpuComboBoxListener;
import de.patrickmetz.bean8.gui.listener.LoadRomButtonListener;
import de.patrickmetz.bean8.gui.listener.PauseButtonListener;
import de.patrickmetz.bean8.gui.listener.StopButtonListener;
import de.patrickmetz.bean8.gui.timer.FpsTimer;
import de.patrickmetz.bean8.runner.Runner;
import de.patrickmetz.bean8.runner.event.RunnerEvent;
import de.patrickmetz.bean8.runner.event.RunnerEventListener;
import de.patrickmetz.bean8.runner.event.RunnerStatus;

import javax.swing.*;
import java.awt.*;

public class Gui implements RunnerEventListener {

    private static Runner runner;
    private static Window window;

    private JPanel bottomPanel;
    private JPanel centerPanel;
    private CpuComboBox cpuComboBox;
    private Display display;
    private FileChooser fileChooser;
    private FpsTimer fpsTimer;
    private JButton loadRomButton;
    private PauseButton pauseButton;
    private StatusPane statusPane;
    private StopButton stopButton;
    private JPanel topPanel;
    private JPanel windowContent;

    private Gui() {
        createComponents();
        createFpsTimer();
        createListeners();
        initializeComponents();

        runner.setDisplay(display);
    }

    // see: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
    public static void show(Runner runner) {
        Gui.runner = runner;

        SwingUtilities.invokeLater(Gui::createGui);

        if (!runner.getRomPath().isBlank()) {
            SwingUtilities.invokeLater(runner::run);
        }
    }

    @Override
    public void handleRunnerEvent(RunnerEvent e) {
        if (e.getStatus() == RunnerStatus.STOPPED) {
            resetDisplay();
        }
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
    }

    private void createComponents() {
        // structure

        windowContent = new WindowContent();

        topPanel = new TopPanel();
        windowContent.add(topPanel, BorderLayout.NORTH);

        centerPanel = new CenterPanel();
        windowContent.add(centerPanel, BorderLayout.CENTER);

        bottomPanel = new BottomPanel();
        windowContent.add(bottomPanel, BorderLayout.SOUTH);

        // interaction

        fileChooser = new FileChooser();

        loadRomButton = new LoadRomButton();
        topPanel.add(loadRomButton);

        pauseButton = new PauseButton();
        topPanel.add(pauseButton);

        stopButton = new StopButton();
        topPanel.add(stopButton);

        cpuComboBox = new CpuComboBox();
        topPanel.add(cpuComboBox);

        // output

        display = new Display();
        centerPanel.add(display);

        statusPane = new StatusPane();
        bottomPanel.add(statusPane);
    }

    private void createFpsTimer() {
        fpsTimer = new FpsTimer(display, statusPane);
    }

    private void createListeners() {
        loadRomButton.addActionListener(
                new LoadRomButtonListener(runner, fileChooser)
        );

        PauseButtonListener pauseButtonListener = new PauseButtonListener(runner, pauseButton);
        StopButtonListener stopButtonListener = new StopButtonListener(runner, stopButton);
        CpuComboBoxListener cpuComboBoxListener = new CpuComboBoxListener(runner, cpuComboBox);

        pauseButton.addActionListener(pauseButtonListener);
        stopButton.addActionListener(stopButtonListener);
        cpuComboBox.addItemListener(cpuComboBoxListener);

        runner.addListener(cpuComboBoxListener);
        runner.addListener(pauseButtonListener);
        runner.addListener(stopButtonListener);

        runner.addListener(statusPane);
        runner.addListener(fpsTimer);
        runner.addListener(this);
    }

    private void initializeComponents() {
        cpuComboBox.setSelectedItem(
                runner.getLegacyMode() ?
                        CpuComboBox.CPU_VIP : CpuComboBox.CPU_SCHIP
        );

    }

    private void resetDisplay() {
        centerPanel.remove(display);

        display = new Display();
        centerPanel.add(display);

        runner.setDisplay(display);
    }

}
