/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:13.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8.emulator.hardware;

public interface CentralProcessingUnit {

    boolean[][] getDisplayData();

    void processNextInstruction() throws UnsupportedOperationException;

    void setProgramCounter(int memoryAddress);

    void updateTimers();

    void writeToMemory(int[] data, int memoryAddress);

}