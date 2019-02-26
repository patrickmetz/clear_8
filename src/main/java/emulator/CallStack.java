/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 26.02.19 13:41.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.Stack;

final class CallStack {

    private final Stack<Short> memory;

    CallStack() {
        memory = new Stack<>();
    }

    Short pop() {
        return memory.pop();
    }

    void push(Short value) {
        memory.push(value);
    }

}