/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 13:18.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import de.patrickmetz.bean8.gui.Gui;

final public class Main {

    private static final int INSTRUCTIONS_PER_SECOND = 500;

    public static void main(String[] args) {
        Arguments arg = new Arguments(args, "bean8");
        arg.expect(String.class, false, "r", "rom", "Path of a ROM file.");
        arg.expect(Integer.class, false, "i", "ips", "Instructions per second.");
        arg.expect(Boolean.class, false, "l", "leg", "Legacy mode.");

        String romPath = arg.toString("rom");
        int instructionsPerSecond = arg.toInteger("ips", INSTRUCTIONS_PER_SECOND);
        boolean legacyMode = arg.toBoolean("leg", false);

        Gui.render((new Runner(
                romPath,
                instructionsPerSecond,
                legacyMode
        )));
    }
}
