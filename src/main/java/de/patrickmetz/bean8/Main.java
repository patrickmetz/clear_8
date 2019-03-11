/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 11.03.19 14:49.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import de.patrickmetz.bean8.gui.Gui;
import de.patrickmetz.bean8.runner.Runner;

final public class Main {

    private static final int DEFAULT_IPS = 500;

    private static final String DESCRIPTION_IPS = "CPU instructions per second. (100, 200, ...)";
    private static final String DESCRIPTION_ROM = "File path of a ROM file.";
    private static final String DESCRIPTION_VIP = "Use VIP CPU (SCHIP otherwise). (true / false)";

    public static void main(String[] args) {
        Arguments arg = new Arguments(args, "bean8");
        arg.expect(String.class, false, "r", "rom", DESCRIPTION_ROM);
        arg.expect(Integer.class, false, "i", "ips", DESCRIPTION_IPS);
        arg.expect(Boolean.class, false, "l", "vip", DESCRIPTION_VIP);

        String romPath = arg.toString("rom");
        int instructionsPerSecond = arg.toInteger("ips", DEFAULT_IPS);
        boolean useVipCpu = arg.toBoolean("vip", false);

        Gui.show((new Runner(
                romPath,
                instructionsPerSecond,
                useVipCpu
        )));
    }

}
