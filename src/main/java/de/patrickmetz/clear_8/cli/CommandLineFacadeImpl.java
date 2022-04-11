package de.patrickmetz.clear_8.cli;

import de.patrickmetz.clear_8.globals.Text;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public final class CommandLineFacadeImpl implements CommandLineFacade {
    private final String[] arguments;
    private final Options  options;

    private CommandLine commandLine;

    public CommandLineFacadeImpl(String[] commandLineArguments) {
        this.arguments = commandLineArguments;

        options = new Options();
    }

    @Override
    public void expectOption(
            String name,
            String helpText,
            Class<?> dataType,
            boolean hasArgument
    ) {
        Option option = Option
                .builder(getShortName(name))
                .longOpt(name)
                .desc(helpText)
                .type(dataType)
                .required(false)
                .hasArg(hasArgument)
                .build();

        options.addOption(option);
    }

    private String getShortName(String longName) {
        return longName.substring(0, 1);
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
        new HelpFormatter().printHelp(Text.Cli.APPLICATION_NAME, options);
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