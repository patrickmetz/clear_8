/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 25.02.19 18:37.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.Stack;

final class CallStack {

    private final Stack<Byte> memory;

    CallStack() {
        memory = new Stack<>();
    }

    byte pop() {
        return memory.pop();
    }

    void push(byte value) {
        memory.push(value);
    }

}