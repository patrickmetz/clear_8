package de.patrickmetz.clear_8.emulator;

import de.patrickmetz.clear_8.emulator.events.EmulatorEvent;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventImpl;
import de.patrickmetz.clear_8.emulator.events.EmulatorEventListener;
import de.patrickmetz.clear_8.emulator.events.EmulatorState;
import de.patrickmetz.clear_8.emulator.hardware.CPU;
import de.patrickmetz.clear_8.emulator.hardware.CPUFactory;
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
 */
final public class EmulatorImpl extends SwingWorker<Void, boolean[][]> implements Emulator {
    private EventListenerList listeners;

    private final float FRAME_DURATION = 1000f / Config.Emulator.FRAMES_PER_SECOND;

    private final int MEMORY_OFFSET_FONT = 0;
    private final int MEMORY_OFFSET_GAME = 512;

    private Display  display;
    private Keyboard keyboard;

    private boolean isPaused;
    private boolean isRunning;

    private String  gamePath;
    private int     instructionsPerSecond;
    private boolean useVipCpu;

    private int instructionCountPerFrame;

    public EmulatorImpl(String gamePath, int instructionsPerSecond, boolean useVipCpu) {
        this.gamePath = gamePath;
        this.instructionsPerSecond = instructionsPerSecond;
        this.useVipCpu = useVipCpu;

        updateInstructionCountPerFrame();

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
        updateInstructionCountPerFrame();
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

        if (isPaused) {
            notifyListeners(EmulatorState.PAUSED);
        } else {
            synchronized (this) { // acquire intrinsic lock / get mutually exclusive access
                notify(); // wakes a single pausing thread up
            }
            notifyListeners(EmulatorState.RESUMED);
        }
    }

    // state events -----------------------------------------------------------

    @Override
    public void addStateListener(EmulatorEventListener listener) {
        listeners.add(EmulatorEventListener.class, listener);
    }

    private void notifyListeners(EmulatorState runnerStatus) {
        EmulatorEvent runnerEvent = new EmulatorEventImpl(this, runnerStatus);

        EmulatorEventListener[] listenerList =
                listeners.getListeners(EmulatorEventListener.class);

        for (EmulatorEventListener listener : listenerList) {
            listener.handleEmulatorEvent(runnerEvent);
        }
    }

    // the main loop ----------------------------------------------------------

    /**
     * Here the emulator does its actual work, as a Swing worker
     */
    @Override
    public Void doInBackground() throws InterruptedException, IOException {
        CPU cpu = CPUFactory.makeCpu(useVipCpu, keyboard);

        cpu.setMemory(Font.getBytes(), MEMORY_OFFSET_FONT);
        cpu.setMemory(loadFileAsByteArray(gamePath), MEMORY_OFFSET_GAME);
        cpu.setProgramCounter(MEMORY_OFFSET_GAME);

        long timeOfLastFrameEnd = System.currentTimeMillis();

        while (!isCancelled()) {
            synchronized (this) { // acquire intrinsic lock / get mutually exclusive access
                while (isPaused) wait(); // cpu-friendly conditional thread pausing
            }

            cpu.process(instructionCountPerFrame);
            publish(cpu.getDisplayData()); // let Swing schedule intermediate result processing

            waitUntilFrameEnds(timeOfLastFrameEnd);
            timeOfLastFrameEnd = System.currentTimeMillis();
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

    private int[] loadFileAsByteArray(String filePath) throws IOException {
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

    private void waitUntilFrameEnds(long endOfFrameTime) throws InterruptedException {
        // todo: the emulator cannot keep steady 60 fps, because the floating point precision
        //  of FRAME_DURATION is lost when used with the long result of currentTimeMillis().
        //  and using nanoTime() would end up way too expensive, and does only guarantee milli
        //  second resolution.
        while (System.currentTimeMillis() < endOfFrameTime + (long) FRAME_DURATION) {
            //  todo: Thread.sleep(1) is too imprecisely scheduled and relatively expensive
            Thread.sleep(1);
        }
    }

    private void updateInstructionCountPerFrame() {
        instructionCountPerFrame = instructionsPerSecond / Config.Emulator.FRAMES_PER_SECOND;
    }
}
