package de.patrickmetz.clear_8.emulator.hardware;

import java.util.Stack;

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