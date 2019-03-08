/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 08.03.19 14:50.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.action.*;
import de.patrickmetz.bean8.gui.component.Window;
import de.patrickmetz.bean8.gui.component.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Gui {

    private static final String APPLICATION_TITLE = "bean8";

    private static Runner runner;
    private static JFrame window;

    private JPanel bottomPanel;
    private JComboBox<String> cpuComboBox;
    private Display display;
    private JTextPane fpsPane;
    private Timer fpsTimer;
    private JButton loadRomButton;
    private JPanel menuPanel;
    private JButton pauseButton;
    private JPanel screen;
    private JTextPane statusPane;
    private JButton stopButton;
    private JPanel windowContent;

    private Gui() {
        createComponents();
        setComponentsUp();
        createListeners();
        createTimers();

        runner.setDisplay(display);
    }

    public static void show(Runner runner) {
        Gui.runner = runner;

        // see: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
        SwingUtilities.invokeLater(Gui::createGui);

        if (runner.getRomPath() != null) {
            SwingUtilities.invokeLater(runner::run);
        }
    }

    public void resetDisplay() {
        screen.remove(display);

        display = new Display();
        screen.add(display);
        runner.setDisplay(display);
    }

    private static void createGui() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        window = new Window(APPLICATION_TITLE);
        window.setContentPane(new Gui().windowContent);
        window.pack();
    }

    private void createComponents() {
        windowContent = new windowContent();

        menuPanel = new MenuPanel();
        windowContent.add(menuPanel, BorderLayout.NORTH);

        screen = new Screen();
        windowContent.add(screen, BorderLayout.CENTER);

        display = new Display();
        screen.add(display);

        bottomPanel = new BottomPanel();
        windowContent.add(bottomPanel, BorderLayout.SOUTH);

        loadRomButton = new LoadRomButton();
        menuPanel.add(loadRomButton);

        pauseButton = new PauseButton();
        menuPanel.add(pauseButton);

        stopButton = new StopButton();
        menuPanel.add(stopButton);

        cpuComboBox = new CpuComboBox();
        menuPanel.add(cpuComboBox);

        statusPane = new StatusPane();
        bottomPanel.add(statusPane, BorderLayout.WEST);

        fpsPane = new FpsPane();
        bottomPanel.add(fpsPane, BorderLayout.EAST);
    }

    private void createListeners() {
        loadRomButton.addActionListener(
                new LoadRomButtonAction(runner, statusPane, pauseButton, stopButton, cpuComboBox)
        );

        pauseButton.addActionListener(
                new PauseButtonAction(runner)
        );

        stopButton.addActionListener(
                new StopButtonAction(runner, pauseButton, this, fpsTimer, cpuComboBox)
        );

        cpuComboBox.addItemListener(
                new CpuComboBoxListener(runner)
        );
    }

    private void createTimers() {
        fpsTimer = new Timer(
                1000,
                new FpsTimerAction(display, fpsPane)
        );

        fpsTimer.start();
    }

    private void setComponentsUp() {
        String romPath = runner.getRomPath();

        if (!romPath.isBlank()) {
            statusPane.setText(
                    new File(romPath).getName()
            );

            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        }

        if (runner.getLegacyMode()) {
            cpuComboBox.setSelectedItem(CpuComboBox.CPU_COSMAC_VIP);
        } else {
            cpuComboBox.setSelectedItem(CpuComboBox.CPU_SUPER_CHIP);
        }
    }
}
