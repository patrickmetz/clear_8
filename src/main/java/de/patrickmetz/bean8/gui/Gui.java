/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 20:58.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.component.interaction.FileDialog;
import de.patrickmetz.bean8.gui.component.interaction.*;
import de.patrickmetz.bean8.gui.component.output.Display;
import de.patrickmetz.bean8.gui.component.output.StatusPane;
import de.patrickmetz.bean8.gui.component.structure.Window;
import de.patrickmetz.bean8.gui.component.structure.*;
import de.patrickmetz.bean8.gui.listener.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Gui {

    private static Runner runner;
    private static Window window;

    private JPanel bottomPanel;
    private JPanel centerPanel;
    private JComboBox<String> cpuComboBox;
    private Display display;
    private FileDialog fileDialog;
    private JTextPane fpsPane;
    private Timer fpsTimer;
    private JButton loadRomButton;
    private JButton pauseButton;
    private StatusPane statusPane;
    private JButton stopButton;
    private JPanel topPanel;
    private JPanel windowContent;

    private Gui() {
        createComponents();
        setComponentsUp();
        createListeners();
        createTimers();

        runner.setDisplay(display);
    }

    // see: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
    public static void show(Runner runner) {
        Gui.runner = runner;

        SwingUtilities.invokeLater(Gui::createGui);

        if (runner.getRomPath() != null) {
            SwingUtilities.invokeLater(runner::run);
        }
    }

    public void resetDisplay() {
        centerPanel.remove(display);

        display = new Display();
        centerPanel.add(display);

        runner.setDisplay(display);

        stopTimers();
        createTimers();
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

        fileDialog = new FileDialog();

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

    private void createListeners() {
        loadRomButton.addActionListener(
                new LoadRomButtonListener(
                        runner, statusPane, pauseButton, stopButton, cpuComboBox, fileDialog)
        );

        pauseButton.addActionListener(
                new PauseButtonListener(runner)
        );

        stopButton.addActionListener(
                new StopButtonListener(runner, pauseButton, this, cpuComboBox, statusPane)
        );

        cpuComboBox.addItemListener(
                new CpuComboBoxListener(runner)
        );
    }

    private void createTimers() {
        fpsTimer = new Timer(
                1000,
                new FpsTimerListener(display, statusPane)
        );

        fpsTimer.start();
    }

    private void setComponentsUp() {
        String romPath = runner.getRomPath();

        if (!romPath.isBlank()) {
            statusPane.setFileName(
                    new File(romPath).getName()
            );

            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        }

        if (runner.getLegacyMode()) {
            cpuComboBox.setSelectedItem(
                    CpuComboBox.CPU_COSMAC_VIP
            );
        } else {
            cpuComboBox.setSelectedItem(
                    CpuComboBox.CPU_SUPER_CHIP
            );
        }
    }

    private void stopTimers() {
        fpsTimer.stop();
    }

}
