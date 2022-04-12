package de.patrickmetz.clear_8.emulator.hardware;

public interface CPU {

    void process(int instructionCount);

    void setProgramCounter(int memoryAddress);

    void setMemory(int[] data, int memoryAddress);

    boolean[][] getDisplayData();

}