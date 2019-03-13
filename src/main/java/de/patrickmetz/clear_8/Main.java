/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8;

import de.patrickmetz.clear_8.gui.Gui;
import de.patrickmetz.clear_8.runner.Runner;

final public class Main {

    private static final String APPLICATION_NAME = "clear_8";

    private static final int DEFAULT_IPS = 500;
    private static final boolean DEFAULT_VIP = false;

    private static final String DESCRIPTION_HELP = "Shows how to use the command line options.";
    private static final String DESCRIPTION_IPS = "CPU instructions per second. (100, 200, ...)";
    private static final String DESCRIPTION_ROM = "File path of a ROM file. (C:\\example\\rom.ch8)";
    private static final String DESCRIPTION_VIP = "Use VIP CPU; SCHIP CPU otherwise. (true / false)";

    public static void main(String[] args) {
        Arguments arguments = new Arguments(args, APPLICATION_NAME);

        arguments.expect("h", "help", DESCRIPTION_HELP, String.class, false);
        arguments.expect("r", "rom", DESCRIPTION_ROM, String.class, true);
        arguments.expect("i", "ips", DESCRIPTION_IPS, Integer.class, true);
        arguments.expect("v", "vip", DESCRIPTION_VIP, Boolean.class, true);

        if (arguments.given("help")) {
            arguments.showHelp();
            return;
        }

        Gui.show((new Runner(
                arguments.asString("rom"),
                arguments.asInteger("ips", DEFAULT_IPS),
                arguments.asBoolean("vip", DEFAULT_VIP)
        )));
    }

}
