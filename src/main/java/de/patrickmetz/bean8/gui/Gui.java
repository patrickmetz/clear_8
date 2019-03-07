/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 07.03.19 20:32.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.Runner;
import de.patrickmetz.bean8.gui.action.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Gui {

    private static Runner runner;

    private JPanel bottomPanel;
    private JPanel contentPane;
    private JComboBox<String> cpuComboBox;
    private JTextPane fpsPane;
    private Timer fpsTimer;
    private JButton loadRomButton;
    private JPanel menuPanel;
    private JButton pauseButton;
    private Screen screen;
    private JPanel screenArea;
    private JTextPane statusPane;
    private JButton stopButton;

    private Gui() {
        setLoadRomButtonListener();
        setPauseButtonListener();
        setStopButtonListener();
        setCpuComboBoxListener();

        preparePauseButton();
        prepareStopButton();
        prepareCpuComboBox();
        prepareStatusPane();
        prepareScreen();

        prepareFpsTimer();
    }

    /**
     * Creates the Graphical User Interface (GUI) and keeps it responsive by putting it on Swings
     * Event Dispatch Thread (EDT).
     * <p>
     * see: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
     */
    public static void render(Runner runner) {
        Gui.runner = runner;

        SwingUtilities.invokeLater(() -> {
            prepareGui();

            if (runner.getRomPath() != null) {
                runner.run();
            }
        });
    }

    public void resetFpsTimer() {
        fpsTimer.stop();
        prepareFpsTimer();
    }

    public void resetScreen() {
        screenArea.remove(screen);

        screen = new Screen();
        screenArea.add(screen);
        runner.setScreen(screen);
    }

    private static JFrame createWindow(JPanel contentPane) {
        JFrame window = new JFrame("bean8");
        window.setContentPane(contentPane);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();

        return window;
    }

    private static void prepareGui() {
        prepareWindow(
                createWindow(
                        new Gui().contentPane
                )
        );
    }

    private static void prepareWindow(JFrame window) {
        window.setLocationRelativeTo(null); // centers window
        window.setResizable(false);
        window.setVisible(true);
    }

    private void prepareCpuComboBox() {
        cpuComboBox.addItem("CPU");

        cpuComboBox.addItem(CpuComboBoxListener.TEXT_SUPER_CHIP);
        cpuComboBox.addItem(CpuComboBoxListener.TEXT_COSMAC_VIP);

        if (runner.getLegacyMode()) {
            cpuComboBox.setSelectedItem(CpuComboBoxListener.TEXT_COSMAC_VIP);
        } else {
            cpuComboBox.setSelectedItem(CpuComboBoxListener.TEXT_SUPER_CHIP);
        }
    }

    private void prepareFpsTimer() {
        fpsTimer = new Timer(
                1000,
                new FpsTimerAction(screen, fpsPane)
        );

        fpsTimer.start();
    }

    private void preparePauseButton() {
        if (!runner.getRomPath().isBlank()) {
            pauseButton.setEnabled(true);
        }
    }

    private void prepareScreen() {
        screenArea.setPreferredSize(new Dimension(640, 480));
        screen = new Screen();

        screenArea.add(screen);
        runner.setScreen(screen);
    }

    private void prepareStatusPane() {
        String romPath = runner.getRomPath();

        if (romPath != null) {
            statusPane.setText(
                    new File(romPath).getName()
            );
        }
    }

    private void prepareStopButton() {
        if (!runner.getRomPath().isBlank()) {
            stopButton.setEnabled(true);
        }
    }

    private void setCpuComboBoxListener() {
        cpuComboBox.addItemListener(
                new CpuComboBoxListener(runner)
        );
    }

    private void setLoadRomButtonListener() {
        loadRomButton.addActionListener(
                new LoadRomButtonAction(runner, statusPane, pauseButton, stopButton, cpuComboBox)
        );
    }

    private void setPauseButtonListener() {
        pauseButton.addActionListener(
                new PauseButtonAction(runner)
        );
    }

    private void setStopButtonListener() {
        stopButton.addActionListener(
                new StopButtonAction(runner, pauseButton, this, fpsTimer, cpuComboBox)
        );
    }

}
