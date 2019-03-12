/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 12:37.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import de.patrickmetz.bean8.gui.Gui;
import de.patrickmetz.bean8.runner.Runner;

final public class Main {

    private static final String APPLICATION_NAME = "bean8";

    private static final int DEFAULT_IPS = 500;
    private static final boolean DEFAULT_VIP_CPU = false;

    private static final String DESCRIPTION_IPS = "CPU instructions per second. (100, 200, ...)";
    private static final String DESCRIPTION_ROM = "File path of a ROM file.";
    private static final String DESCRIPTION_VIP = "Use VIP CPU (SCHIP otherwise). (true / false)";

    public static void main(String[] args) {
        Arguments arguments = new Arguments(args, APPLICATION_NAME);

        arguments.expect("r", "rom", DESCRIPTION_ROM, String.class, false);
        arguments.expect("i", "ips", DESCRIPTION_IPS, Integer.class, false);
        arguments.expect("v", "vip", DESCRIPTION_VIP, Boolean.class, false);

        Gui.show((new Runner(
                arguments.asString("rom"),
                arguments.asInteger("ips", DEFAULT_IPS),
                arguments.asBoolean("vip", DEFAULT_VIP_CPU)
        )));
    }

}
