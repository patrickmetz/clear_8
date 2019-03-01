/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 01.03.19 20:05.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

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