/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 13:59.
 * Copyright (c) 2019. All rights reserved.
 */

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