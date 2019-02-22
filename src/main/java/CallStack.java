/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 22.02.19 17:22.
 * Copyright (c) 2019. All rights reserved.
 */

import java.util.Stack;

public class CallStack {

    private Stack<Byte> memory;

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