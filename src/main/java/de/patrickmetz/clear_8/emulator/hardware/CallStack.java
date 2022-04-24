package de.patrickmetz.clear_8.emulator.hardware;

import java.util.Stack;

/**
 * Contains memory addresses used by the program counter, to enter and leave
 * (nested) subroutine calls.
 * <p>
 * Entering a subroutine involves pushing the current memory address, in order
 * to be able to jump back there, after the subroutine ends.
 * <p>
 * Leaving a subroutine means popping an address to continue program execution
 * at a previous memory state.
 */
final class CallStack {

    private final Stack<Integer> memory;

    CallStack() {
        memory = new Stack<>();
    }

    int pop() {
        return memory.pop();
    }

    void push(int value) {
        memory.push(value);
    }

}