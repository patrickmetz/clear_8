package de.patrickmetz.clear_8;

import de.patrickmetz.clear_8.gui.Gui;
import de.patrickmetz.clear_8.runner.Runner;

final public class Main {
    private static final String HELP_HELP = "Shows how to use the command line options.";
    private static final String HELP_IPS  = "CPU instructions per second. (100, 200, ...)";
    private static final String HELP_ROM  = "File path of a ROM file. (C:\\example\\filename)";
    private static final String HELP_VIP  = "Use Cosmac VIP CPU if true; or Super Chip CPU otherwise. (true / false)";

    private static final String HELP = "help";
    private static final String IPS  = "ips";
    private static final String ROM  = "rom";
    private static final String VIP  = "vip";

    private static final int     DEFAULT_IPS = 500;
    private static final boolean DEFAULT_VIP = false;

    public static void main(String[] args) {
        CliFacade cli = new CliFacade(args);

        cli.expect("h", HELP, HELP_HELP, String.class, false);
        cli.expect("i", IPS, HELP_IPS, Integer.class, true);
        cli.expect("r", ROM, HELP_ROM, String.class, true);
        cli.expect("v", VIP, HELP_VIP, Boolean.class, true);

        if (cli.given(HELP)) {
            cli.printHelp();
            return;
        }

        Gui.show(new Runner(
                cli.get(ROM),
                cli.get(IPS, DEFAULT_IPS),
                cli.get(VIP, DEFAULT_VIP)
        ));
    }
}
