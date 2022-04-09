package de.patrickmetz.clear_8;

import org.apache.commons.cli.*;

/**
 * Hides the complexity of the Apache command line
 * parser subsystem and simplifies its usage.
 */
final class CliFacade {
    private final String[] arguments;
    private final Options  options;

    private CommandLine commandLine;

    /**
     * @param arguments The arguments, delivered by the main method.
     */
    CliFacade(String[] arguments) {
        this.arguments = arguments;

        options = new Options();
    }

    /**
     * Registers an expected command line option.
     */
    public void expect(String shortName,
                       String longName,
                       String description,
                       Class<?> type,
                       boolean hasArgument) {
        Option option = Option
                .builder(shortName)
                .longOpt(longName)
                .desc(description)
                .type(type)
                .required(false)
                .hasArg(hasArgument)
                .build();

        options.addOption(option);
    }

    /**
     * Tests if a command line option was given.
     */
    public boolean given(String optionName) {
        return getCommandLine().hasOption(optionName);
    }

    /**
     * Either gets the value of a given command line option as a string,
     * or returns null, if it wasn't given.
     */
    public String get(String optionName) {
        return getCommandLine().getOptionValue(optionName);
    }

    /**
     * Either gets the value of a given command line option as an integer,
     * or returns the given integer default value, if it wasn't given.
     */
    public int get(String optionName, int defaultValue) {
        CommandLine commandLine = getCommandLine();

        return commandLine.hasOption(optionName)
                ? Integer.parseInt(commandLine.getOptionValue(optionName))
                : defaultValue;
    }

    /**
     * Either gets the value of a given command line option as a boolean,
     * or returns the given boolean default value, if it wasn't given.
     */
    public boolean get(String optionName, boolean defaultValue) {
        CommandLine commandLine = getCommandLine();

        return commandLine.hasOption(optionName)
                ? Boolean.parseBoolean(commandLine.getOptionValue(optionName))
                : defaultValue;
    }

    /**
     * Shows the possible command line usage,
     * for the expected command line options.
     */
    public void printHelp() {
        new HelpFormatter().printHelp("clear_8", options);
    }

    /**
     * Either gives access to the parsed given command line options,
     * or shows the correct usage of the command line options.
     */
    private CommandLine getCommandLine() {
        if (commandLine == null) {
            try {
                commandLine = new DefaultParser().parse(options, arguments);
            } catch (ParseException e) {
                // TODO: why is this here? does it really make sense?
                printHelp();
                System.exit(1);
            }
        }

        return commandLine;
    }
}