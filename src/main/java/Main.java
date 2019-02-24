/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 24.02.19 03:13.
 * Copyright (c) 2019. All rights reserved.
 */

import java.io.FileNotFoundException;

public class Main {

    private static final int INSTRUCTIONS_PER_SECOND = 500;

    public static void main(String[] args) {
        Arguments arg = new Arguments(args, "chip8-java");
        arg.expect(String.class, true, "r", "rom", "Path of a ROM file.");
        arg.expect(Integer.class, false, "i", "ips", "Instructions per second.");

        String rom = arg.toString("rom");
        int ips = arg.toInteger("ips", INSTRUCTIONS_PER_SECOND);

        Emulator emulator = new Emulator(ips);

        try {
            emulator.run(rom);
        } catch (FileNotFoundException e) {
            //todo: log this
            e.printStackTrace();
        } catch (InterruptedException e) {
            //todo: log this
            e.printStackTrace();
        }
    }

}
