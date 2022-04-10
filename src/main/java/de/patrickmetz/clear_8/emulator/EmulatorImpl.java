package de.patrickmetz.clear_8.emulator;

import de.patrickmetz.clear_8.emulator.events.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventImpl;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.events.EmulatorState;
import de.patrickmetz.clear_8.emulator.hardware.CentralProcessingUnit;
import de.patrickmetz.clear_8.emulator.hardware.CentralProcessingUnitFactory;
import de.patrickmetz.clear_8.emulator.input.Keyboard;
import de.patrickmetz.clear_8.gui.output.Display;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * The emulator basically loops forever, or until it is shut down for good.
 * In order for the GUI to not completely freeze, while waiting for the
 * emulator to return something, the emulator needs to be put on a separate Thread.
 *
 * But it needs to be a special thread, ensuring that we only update the
 * GUI when it's ready to be updated. So we extend SwingWorker and implement
 * or use the necessary methods:
 * <p><p>
 * doInBackground() is the method where a SwingWorker does its work
 * (the emulation loop in our case).
 * <p><p>
 * publish() sends intermediate work results from within doInBackground()
 * (display data in our case) to the GUI, where they are scheduled for
 * later processing.
 * <p><p>
 * isCancelled() is used from within doInBackground(), to check if the
 * emulator was shut down from the gui side.
 * <p><p>
 * process() is called by the GUI from the outside, whenever it is ready
 * to be updated with some of the already collected work results.
 * <p><p>
 * The Java Generic means that the doInBackground() method returns nothing
 * (Void) when it ends, and that the publish() method returns a two dimensional
 * array of truth values (boolean[][]) which is send to process().
 * <p><p>
 * see https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
 */
final public class EmulatorImpl extends SwingWorker<Void, boolean[][]> implements Emulator {
    private EventListenerList listeners;

    private final int FRAMES_PER_SECOND = 60;
    private final int FRAME_DURATION    = 1000 / FRAMES_PER_SECOND;
    private       int instructionsPerFrame;

    private final int MEMORY_OFFSET_FONT = 0;
    private final int MEMORY_OFFSET_ROM  = 512;

    private CentralProcessingUnit cpu;

    private Display  display;
    private Keyboard keyboard;

    private boolean isPaused;
    private boolean isRunning;

    private String  gamePath;
    private int     instructionsPerSecond;
    private boolean useVipCpu;

    public EmulatorImpl(String gamePath, int instructionsPerSecond, boolean useVipCpu) {
        this.gamePath = gamePath;
        this.instructionsPerSecond = instructionsPerSecond;
        this.useVipCpu = useVipCpu;

        listeners = new EventListenerList();
    }

    // setters for in- and output ---------------------------------------------

    @Override
    public void setDisplay(Display display) {
        this.display = display;
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    // getters and setter for options -----------------------------------------

    @Override
    public String getGamePath() {
        return gamePath;
    }

    @Override
    public void setGamePath(String gamePath) {
        this.gamePath = gamePath;
    }

    @Override
    public int getInstructionsPerSecond() {
        return instructionsPerSecond;
    }

    @Override
    public void setInstructionsPerSecond(int instructionsPerSecond) {
        this.instructionsPerSecond = instructionsPerSecond;
    }

    @Override
    public boolean getUseVipCpu() {
        return useVipCpu;
    }

    @Override
    public void setUseVipCpu(boolean useVipCpu) {
        this.useVipCpu = useVipCpu;
    }

    // state control ----------------------------------------------------------

    @Override
    public void start() {
        if (isRunning) {
            return;
        }

        isRunning = true;

        cpu = CentralProcessingUnitFactory.makeCpu(useVipCpu, keyboard);

        instructionsPerFrame = instructionsPerSecond / FRAMES_PER_SECOND;

        this.execute();

        notify(EmulatorState.STARTED);
    }

    @Override
    public void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;
        isPaused = false;

        this.cancel(true);

        notify(EmulatorState.STOPPED);
    }

    /**
     * thread safe pause toggling
     * <p>
     * see: https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html
     */
    public synchronized void togglePause() {
        if (!isRunning) {
            return;
        }

        isPaused = !isPaused;

        notify();

        if (isPaused) {
            notify(EmulatorState.PAUSED);
        } else {
            notify(EmulatorState.RESUMED);
        }
    }

    // state events -----------------------------------------------------------

    @Override
    public void addStateListener(EmulatorEventListener listener) {
        listeners.add(EmulatorEventListener.class, listener);
    }

    private synchronized void notify(EmulatorState runnerStatus) {
        EmulatorEvent runnerEvent = new EmulatorEventImpl(this, runnerStatus);

        EmulatorEventListener[] listenerList =
                listeners.getListeners(EmulatorEventListener.class);

        for (EmulatorEventListener listener : listenerList) {
            listener.handleEmulatorEvent(runnerEvent);
        }
    }

    // the main loop ----------------------------------------------------------

    @Override
    public Void doInBackground() throws InterruptedException, IOException {
        cpu.writeToMemory(Font.getBytes(), MEMORY_OFFSET_FONT);
        cpu.writeToMemory(loadFileAsBytes(gamePath), MEMORY_OFFSET_ROM);
        cpu.setProgramCounter(MEMORY_OFFSET_ROM);

        long now = System.currentTimeMillis();

        while (!isCancelled()) {
            waitUntilPauseEnds();

            for (int i = 0; i < instructionsPerFrame; i++) {
                cpu.processNextInstruction();
            }

            cpu.updateTimers();

            waitUntilFrameEnds(now + FRAME_DURATION);
            now = System.currentTimeMillis();

            // sends screen data to the gui
            publish(cpu.getDisplayData());
        }

        return null;
    }

    /**
     * display data is send back here by the GUI,
     * when it's ready to be updated
     */
    @Override
    protected void process(List<boolean[][]> displayData) {
        for (boolean[][] data : displayData) {
            display.update(data);
        }
    }

    /**
     * loads a file as an array of bytes
     */
    private int[] loadFileAsBytes(String filePath) throws IOException {
        File            file       = new File(filePath);
        int[]           data       = new int[(int) file.length()];
        FileInputStream fileStream = new FileInputStream(file);

        int byteCount = 0;
        int byteAsInteger;

        while ((byteAsInteger = fileStream.read()) != -1) {
            data[byteCount++] = byteAsInteger;
        }

        return data;
    }

    /**
     * resource friendly waiting until a frame ends
     */
    private void waitUntilFrameEnds(long endOfFrameTime) throws InterruptedException {
        while (System.currentTimeMillis() < endOfFrameTime) {
            Thread.sleep(1);
        }
    }

    /**
     * thread safe waiting while paused
     * <p>
     * see: https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html
     */
    private synchronized void waitUntilPauseEnds() throws InterruptedException {
        while (isPaused) {
            wait();
        }
    }
}
