package de.patrickmetz.clear_8.emulator.hardware;

public interface CentralProcessingUnit {

    boolean[][] getDisplayData();

    void processNextInstruction() throws UnsupportedOperationException;

    void setProgramCounter(int memoryAddress);

    void updateTimers();

    void writeToMemory(int[] data, int memoryAddress);

}