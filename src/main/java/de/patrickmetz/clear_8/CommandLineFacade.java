package de.patrickmetz.clear_8;

import org.apache.commons.cli.*;

/**
 * Hides the complexity of the Apache command line
 * option parser subsystem and simplifies its usage.
 */
final class CommandLineFacade {
    private final String[] arguments;
    private final Options  options;

    private CommandLine commandLine;

    CommandLineFacade(String[] commandLineArguments) {
        this.arguments = commandLineArguments;

        options = new Options();
    }

    public void expectOption(
            String shortName,
            String longName,
            String helpText,
            Class<?> dataType,
            boolean hasArgument
    ) {
        Option option = Option
                .builder(shortName)
                .longOpt(longName)
                .desc(helpText)
                .type(dataType)
                .required(false)
                .hasArg(hasArgument)
                .build();

        options.addOption(option);
    }

    public boolean hasOption(String longOrShortName) {
        return getCommandLine().hasOption(longOrShortName);
    }

    public String getOptionValue(String longOrShortName) {
        return getCommandLine().getOptionValue(longOrShortName);
    }

    public int getOptionValueOrDefault(String longOrShortName, int defaultValue) {
        CommandLine commandLine = getCommandLine();

        return commandLine.hasOption(longOrShortName)
                ? Integer.parseInt(commandLine.getOptionValue(longOrShortName))
                : defaultValue;
    }

    public boolean getOptionValueOrDefault(String longOrShortName, boolean defaultValue) {
        CommandLine commandLine = getCommandLine();

        return commandLine.hasOption(longOrShortName)
                ? Boolean.parseBoolean(commandLine.getOptionValue(longOrShortName))
                : defaultValue;
    }

    public void printHelp() {
        new HelpFormatter().printHelp("clear_8.jar", options);
    }

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