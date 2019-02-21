/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 21.02.19 21:56.
 * Copyright (c) 2019. All rights reserved.
 */

public class Main {

    public static void main(String[] args) {
        int instructionsPerSecond = Integer.parseInt(args[0]);

        Interpreter interpreter = new Interpreter(instructionsPerSecond);

        try {
            interpreter.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
