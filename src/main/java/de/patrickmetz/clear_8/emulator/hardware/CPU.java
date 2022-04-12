package de.patrickmetz.clear_8.emulator.hardware;

public interface CPU {

    void process();

    void setInstructionsPerProcessing(int instructionsPerProcessCycle);

    void setProgramCounter(int memoryAddress);

    void setMemory(int[] data, int memoryAddress);

    boolean[][] getDisplayData();

}