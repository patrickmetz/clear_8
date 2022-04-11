package de.patrickmetz.clear_8;

import de.patrickmetz.clear_8.cli.CommandLineFacade;
import de.patrickmetz.clear_8.cli.CommandLineFacadeImpl;
import de.patrickmetz.clear_8.emulator.Emulator;
import de.patrickmetz.clear_8.emulator.EmulatorImpl;
import de.patrickmetz.clear_8.globals.*;
import de.patrickmetz.clear_8.gui.Gui;

/**
 * This is the main entry point of this software.
 * First, it initializes the emulator with command line arguments, given by the
 * user, or default values specified in the config. Then, it starts the graphical
 * user interface with the emulator attached.
 */
final public class Main {
    public static void main(String[] args) {
        CommandLineFacade cl = new CommandLineFacadeImpl(args);

        cl.expectOption(Text.Cli.GAME, Text.Cli.HELP_GAME, String.class, true);
        cl.expectOption(Text.Cli.HELP, Text.Cli.HELP_HELP, String.class, false);
        cl.expectOption(Text.Cli.IPS, Text.Cli.HELP_IPS, Integer.class, true);
        cl.expectOption(Text.Cli.VIP, Text.Cli.HELP_VIP, Boolean.class, true);

        if (cl.hasOption(Text.Cli.HELP)) {
            cl.printExpectedOptions();
            return;
        }

        Emulator emulator = new EmulatorImpl(
                cl.getOptionValue(Text.Cli.GAME),
                cl.getOptionValueOrDefault(Text.Cli.IPS, Config.Cli.DEFAULT_IPS),
                cl.getOptionValueOrDefault(Text.Cli.VIP, Config.Cli.DEFAULT_VIP)
        );

        Gui.show(emulator);
    }
}
