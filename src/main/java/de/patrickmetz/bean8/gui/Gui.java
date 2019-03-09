/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 12:07.
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
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
        createFpsTimer();

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

    private void createFpsTimer() {
        fpsTimer = new Timer(
                1000,
                new FpsTimerListener()
        );

        fpsTimer.start();
    }

    private void createListeners() {
        loadRomButton.addActionListener(new LoadRomButtonListener());
        pauseButton.addActionListener(new PauseButtonListener());
        stopButton.addActionListener(new StopButtonListener());
        cpuComboBox.addItemListener(new CpuComboBoxListener());
    }

    private void setComponentsUp() {
        String romPath = runner.getRomPath();

        if (!romPath.isBlank()) {
            statusPane.setFileName(
                    new File(romPath).getName()
            );

            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            cpuComboBox.setEnabled(false);
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

    private class CpuComboBoxListener implements java.awt.event.ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();

                if (item == CpuComboBox.CPU_COSMAC_VIP) {
                    runner.setLegacyMode(true);
                } else if (item == CpuComboBox.CPU_SUPER_CHIP) {
                    runner.setLegacyMode(false);
                }
            }

        }

    }

    private class FpsTimerListener implements ActionListener {

        private int updateCount;

        @Override
        public void actionPerformed(ActionEvent e) {
            int newUpdateCount = display.getUpdateCount();
            int fps = newUpdateCount - updateCount;
            updateCount = newUpdateCount;

            statusPane.setFps("" + fps);
        }

    }

    private class LoadRomButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileDialog.getFile();

            if (file != null) {

                if (!file.getPath().isBlank()) {
                    if (runner.isRunning()) {
                        runner.stop();
                    }

                    runner.setRomPath(file.getPath());
                    runner.run();

                    pauseButton.setEnabled(true);
                    stopButton.setEnabled(true);
                    cpuComboBox.setEnabled(false);

                    statusPane.setFileName(file.getName());
                    fpsTimer.start();
                }
            }
        }

    }

    private class StopButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton stopButton = (JButton) e.getSource();

            runner.stop();
            resetDisplay();

            stopButton.setEnabled(false);
            pauseButton.setEnabled(false);
            cpuComboBox.setEnabled(true);

            fpsTimer.stop();
            statusPane.clear();
        }

    }

    private class PauseButtonListener implements ActionListener {

        private String TEXT_IS_PAUSED = "Resume";
        private String TEXT_IS_RUNNING = "Pause";

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();

            if (runner.isRunning()) {
                if (runner.isPaused()) {
                    button.setText(TEXT_IS_RUNNING);
                } else {
                    button.setText(TEXT_IS_PAUSED);
                }
            } else {
                return;
            }

            runner.togglePause();
        }

    }

}
