/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 13:06.
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
    private Timer fpsTimer;
    private JButton loadRomButton;
    private JToggleButton pauseButton;
    private StatusPane statusPane;
    private JButton stopButton;
    private JPanel topPanel;
    private JPanel windowContent;

    private Gui() {
        createComponents();
        createListeners();
        initializeComponents();
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

    private void initializeComponents() {
        String romPath = runner.getRomPath();

        if (!romPath.isBlank()) {
            statusPane.setFileName(
                    new File(romPath).getName()
            );

            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            cpuComboBox.setEnabled(false);
        }

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

    private class CpuComboBoxListener implements java.awt.event.ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                runner.setLegacyMode(
                        e.getItem() == CpuComboBox.CPU_VIP
                );
            }

        }

    }

    private class FpsTimerListener implements ActionListener {

        private int updates;

        @Override
        public void actionPerformed(ActionEvent e) {
            int newUpdates = display.getUpdateCount();
            statusPane.setFps("" + (newUpdates - updates));
            updates = newUpdates;
        }

    }

    private class LoadRomButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            File file = fileDialog.getFile();

            if (file != null) {
                runner.stop();
                runner.setRomPath(file.getPath());
                runner.run();

                cpuComboBox.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);

                statusPane.setFileName(file.getName());
                fpsTimer.start();
            }
        }

    }

    private class StopButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            runner.stop();
            resetDisplay();

            cpuComboBox.setEnabled(true);
            pauseButton.setEnabled(false);
            ((StopButton) e.getSource()).setEnabled(false);

            statusPane.clear();
            fpsTimer.stop();
        }

    }

    private class PauseButtonListener implements ActionListener {

        private String TEXT_PAUSE = "Pause";
        private String TEXT_RESUME = "Resume";

        @Override
        public void actionPerformed(ActionEvent e) {
            JToggleButton button = (JToggleButton) e.getSource();

            button.setText(
                    button.isSelected() ? TEXT_RESUME : TEXT_PAUSE
            );

            runner.togglePause();
        }

    }

}
