package de.patrickmetz.clear_8;

import de.patrickmetz.clear_8.cli.CommandLineFacade;
import de.patrickmetz.clear_8.cli.CommandLineFacadeImpl;
import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.EmulatorImpl;
import de.patrickmetz.clear_8.gui.Gui;

/**
 * This is the main entry point of this software.
 * First, it initializes the emulator with command line arguments, given by the
 * user, or default values given by the programmer. Then, it starts the graphical
 * user interface with the emulator attached.
 */
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
        CommandLineFacade cl = new CommandLineFacadeImpl(args);

        cl.expectOption("g", GAME, HELP_GAME, String.class, true);
        cl.expectOption("h", HELP, HELP_HELP, String.class, false);
        cl.expectOption("i", IPS, HELP_IPS, Integer.class, true);
        cl.expectOption("v", VIP, HELP_VIP, Boolean.class, true);

        if (cl.hasOption(HELP)) {
            cl.printExpectedOptions();
            return;
        }

        Emulator emulator = new EmulatorImpl(
                cl.getOptionValue(GAME),
                cl.getOptionValueOrDefault(IPS, DEFAULT_IPS),
                cl.getOptionValueOrDefault(VIP, DEFAULT_VIP)
        );

        Gui.show(emulator);
    }
}
