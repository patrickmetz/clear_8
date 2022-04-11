package de.patrickmetz.clear_8.cli;

import org.apache.commons.cli.*;

public final class CommandLineFacadeImpl implements CommandLineFacade{
    private final String[] arguments;
    private final Options  options;

    private CommandLine commandLine;

    public CommandLineFacadeImpl(String[] commandLineArguments) {
        this.arguments = commandLineArguments;

        options = new Options();
    }

    @Override
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

    @Override
    public boolean hasOption(String longOrShortName) {
        return getCommandLine().hasOption(longOrShortName);
    }

    @Override
    public String getOptionValue(String longOrShortName) {
        return getCommandLine().getOptionValue(longOrShortName);
    }

    @Override
    public int getOptionValueOrDefault(String longOrShortName, int defaultValue) {
        CommandLine commandLine = getCommandLine();

        return commandLine.hasOption(longOrShortName)
                ? Integer.parseInt(commandLine.getOptionValue(longOrShortName))
                : defaultValue;
    }

    @Override
    public boolean getOptionValueOrDefault(String longOrShortName, boolean defaultValue) {
        CommandLine commandLine = getCommandLine();

        return commandLine.hasOption(longOrShortName)
                ? Boolean.parseBoolean(commandLine.getOptionValue(longOrShortName))
                : defaultValue;
    }

    @Override
    public void printExpectedOptions() {
        new HelpFormatter().printHelp("clear_8.jar", options);
    }

    private CommandLine getCommandLine() {
        if (commandLine == null) {
            try {
                commandLine = new DefaultParser().parse(options, arguments);
            } catch (ParseException e) {
                // TODO: why is this here? does it really make sense?
                printExpectedOptions();
                System.exit(1);
            }
        }

        return commandLine;
    }
}