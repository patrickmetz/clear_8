package de.patrickmetz.clear_8;

import de.patrickmetz.clear_8.gui.Gui;
import de.patrickmetz.clear_8.runner.Runner;

final public class Main {
    private static final String HELP_HELP = "Prints help for the command line options.";
    private static final String HELP_IPS  = "CPU instructions per second. Controls emulation speed. (100, 200, ...)";
    private static final String HELP_GAME = "Path to a game file. (C:\\my_folder\\my_game)";
    private static final String HELP_VIP  = "Use Cosmac VIP CPU if true; or Super Chip CPU otherwise. (true / false)";

    private static final String GAME = "game";
    private static final String HELP = "help";
    private static final String IPS  = "ips";
    private static final String VIP  = "vip";

    private static final int     DEFAULT_IPS = 500;
    private static final boolean DEFAULT_VIP = true;

    public static void main(String[] args) {
        CommandLineFacade cl = new CommandLineFacade(args);

        cl.expectOption("g", GAME, HELP_GAME, String.class, true);
        cl.expectOption("h", HELP, HELP_HELP, String.class, false);
        cl.expectOption("i", IPS, HELP_IPS, Integer.class, true);
        cl.expectOption("v", VIP, HELP_VIP, Boolean.class, true);

        if (cl.hasOption(HELP)) {
            cl.printHelp();
            return;
        }

        Gui.show(new Runner(
                cl.getOptionValue(GAME),
                cl.getOptionValueOrDefault(IPS, DEFAULT_IPS),
                cl.getOptionValueOrDefault(VIP, DEFAULT_VIP)
        ));
    }
}
