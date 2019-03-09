/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 09.03.19 17:15.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import de.patrickmetz.bean8.gui.Gui;
import de.patrickmetz.bean8.runner.Runner;

final public class Main {

    private static final int INSTRUCTIONS_PER_SECOND = 500;

    public static void main(String[] args) {
        Arguments arg = new Arguments(args, "bean8");
        arg.expect(String.class, false, "r", "rom", "Path of a ROM file.");
        arg.expect(Integer.class, false, "i", "ips", "Instructions per second.");
        arg.expect(Boolean.class, false, "l", "vip", "Use VIP CPU (SCHIP otherwise).");

        String romPath = arg.toString("rom");
        int instructionsPerSecond = arg.toInteger("ips", INSTRUCTIONS_PER_SECOND);
        boolean useVipCpu = arg.toBoolean("vip", false);

        Gui.show((new Runner(
                romPath,
                instructionsPerSecond,
                useVipCpu
        )));
    }

}
