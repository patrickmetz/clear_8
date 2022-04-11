package de.patrickmetz.clear_8.emulator;

import de.patrickmetz.clear_8.emulator.events.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventImpl;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.events.EmulatorState;
import de.patrickmetz.clear_8.emulator.hardware.CentralProcessingUnit;
import de.patrickmetz.clear_8.emulator.hardware.CentralProcessingUnitFactory;
import de.patrickmetz.clear_8.emulator.input.Keyboard;
import de.patrickmetz.clear_8.emulator.resources.Font;
import de.patrickmetz.clear_8.globals.Config;
import de.patrickmetz.clear_8.gui.output.Display;

import javax.swing.SwingWorker;
import javax.swing.event.EventListenerList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * A SwingWorker is used for long-running background threads, that regularly
 * need to deliver data to Swing, without blocking the GUI.
 *
 * The Generic means that doInBackground() returns nothing (Void), but instead
 * accumulates intermediate work results, as a two-dimensional array (boolean[][]),
 * by calling publish().
 *
 * The Swing GUI notices that, and schedules calls to process(), whenever it is ready
 * to be used with regard to the intermediate work results.
 *
 * see https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html
 */
final public class EmulatorImpl extends SwingWorker<Void, boolean[][]> implements Emulator {
    private EventListenerList listeners;

    private final int FRAME_DURATION = 1000 / Config.Emulator.FRAMES_PER_SECOND;
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

        instructionsPerFrame = instructionsPerSecond / Config.Emulator.FRAMES_PER_SECOND;

        this.execute(); // schedules the SwingWorker, once, as a worker thread

        notifyListeners(EmulatorState.STARTED);
    }

    @Override
    public void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;
        isPaused = false;

        this.cancel(true); // stops the SwingWorker

        notifyListeners(EmulatorState.STOPPED);
    }

    public void togglePause() {
        if (!isRunning) {
            return;
        }

        isPaused = !isPaused;

        notify(); // either wakes up the paused SwingWorker, or does nothing

        if (isPaused) {
            notifyListeners(EmulatorState.PAUSED);
        } else {
            notifyListeners(EmulatorState.RESUMED);
        }
    }

    // state events -----------------------------------------------------------

    @Override
    public void addStateListener(EmulatorEventListener listener) {
        listeners.add(EmulatorEventListener.class, listener);
    }

    private synchronized void notifyListeners(EmulatorState runnerStatus) {
        EmulatorEvent runnerEvent = new EmulatorEventImpl(this, runnerStatus);

        EmulatorEventListener[] listenerList =
                listeners.getListeners(EmulatorEventListener.class);

        for (EmulatorEventListener listener : listenerList) {
            listener.handleEmulatorEvent(runnerEvent);
        }
    }

    // the main loop ----------------------------------------------------------

    /**
     * Here the Swing worker does its actual work
     */
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

            publish(cpu.getDisplayData()); // let Swing schedule intermediate result processing
        }

        return null;
    }

    /**
     * Called by Swing, with chunks of the intermediate results
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
     * waiting until a frame's time span is over
     */
    private void waitUntilFrameEnds(long endOfFrameTime) throws InterruptedException {
        while (System.currentTimeMillis() < endOfFrameTime) {
            Thread.sleep(Config.Emulator.FRAME_SLEEP_INTERVAL_IN_MS);
        }
    }

    /**
     * waiting while paused
     */
    private void waitUntilPauseEnds() throws InterruptedException {
        while (isPaused) {
            wait();
        }
    }
}
