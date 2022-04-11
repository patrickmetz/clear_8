package de.patrickmetz.clear_8.gui;

import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.input.KeyboardImpl;
import de.patrickmetz.clear_8.gui.elements.*;
import de.patrickmetz.clear_8.gui.listeners.*;
import de.patrickmetz.clear_8.gui.output.DisplayImpl;
import de.patrickmetz.clear_8.gui.output.StatusPane;
import de.patrickmetz.clear_8.gui.structure.Window;
import de.patrickmetz.clear_8.gui.structure.*;
import de.patrickmetz.clear_8.gui.timers.FpsTimer;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;

final public class Gui {
    private static boolean  isShown;
    private static Emulator emulator;

    private static Window   window;

    // window structure

    private WindowContent windowContent;

    private TopPanel    topPanel;
    private CenterPanel centerPanel;
    private BottomPanel bottomPanel;

    // interactive elements

    private LoadRomButton loadRomButton;
    private PauseButton   pauseButton;
    private StopButton    stopButton;

    private FileChooser fileChooser;

    private CpuComboBox          cpuComboBox;
    private InstructionsComboBox instructionsComboBox;

    // output elements

    private DisplayImpl display;
    private FpsTimer    fpsTimer;
    private StatusPane statusPane;

    private Gui() {
        constructComponents();
        initializeComponents();

        constructFpsTimer();
        setListenersUp();

        emulator.setDisplay(display);
    }

    public static void show(Emulator emulator) {
        if (isShown) {
            return;
        }
        isShown = true;

        Gui.emulator = emulator;

        // see https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html
        SwingUtilities.invokeLater(Gui::createGui);

        if (Gui.emulator.getGamePath() != null) {
            SwingUtilities.invokeLater(Gui.emulator::start);
        }
    }

    public void resetDisplay() {
        centerPanel.remove(display);

        display = new DisplayImpl();
        centerPanel.add(display);

        emulator.setDisplay(display);
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

        display = new DisplayImpl();
        centerPanel.add(display);

        statusPane = new StatusPane();
        bottomPanel.add(statusPane);
    }

    private void constructFpsTimer() {
        fpsTimer = new FpsTimer(display, statusPane);
    }

    private void initializeComponents() {
        cpuComboBox.setSelectedItem(
                emulator.getUseVipCpu()
        );

        instructionsComboBox.setSelectedItem(
                emulator.getInstructionsPerSecond()
        );
    }

    private void setListenersUp() {

        // create all listeners

        LoadRomButtonListener loadRomButtonListener =
                new LoadRomButtonListener(emulator, fileChooser);

        PauseButtonListener pauseButtonListener =
                new PauseButtonListener(emulator, pauseButton);

        StopButtonListener stopButtonListener =
                new StopButtonListener(emulator, stopButton);

        CpuComboBoxListener cpuComboBoxListener =
                new CpuComboBoxListener(emulator, cpuComboBox);

        InstructionsComboBoxListener instructionsComboBoxListener =
                new InstructionsComboBoxListener(emulator, instructionsComboBox);

        StatusPaneListener statusPaneListener =
                new StatusPaneListener(statusPane);

        GuiListener guiListener = new GuiListener(this);

        // connect mouse click listeners

        loadRomButton.addActionListener(loadRomButtonListener);
        pauseButton.addActionListener(pauseButtonListener);
        stopButton.addActionListener(stopButtonListener);
        cpuComboBox.addItemListener(cpuComboBoxListener);
        instructionsComboBox.addItemListener(instructionsComboBoxListener);

        // connect runner state listeners

        emulator.addStateListener(pauseButtonListener);
        emulator.addStateListener(stopButtonListener);
        emulator.addStateListener(cpuComboBoxListener);
        emulator.addStateListener(instructionsComboBoxListener);

        emulator.addStateListener(statusPaneListener);
        emulator.addStateListener(fpsTimer);

        emulator.addStateListener(guiListener);

        // create and connect keyboard listener

        // can't use keyboard interface because addKeyListener expects an extended class
        KeyboardImpl keyboard = new KeyboardImpl();

        window.addKeyListener(keyboard);
        emulator.setKeyboard(keyboard);
    }
}
