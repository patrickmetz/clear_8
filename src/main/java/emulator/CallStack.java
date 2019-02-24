/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 21:24.
 * Copyright (c) 2019. All rights reserved.
 */

package emulator;

import java.util.Stack;

public class CallStack {

    private Stack<Byte> memory;

    public CallStack() {
        memory = new Stack<>();
    }

    byte pop() {
        return memory.pop();
    }

    void push(byte value) {
        memory.push(value);
    }

}