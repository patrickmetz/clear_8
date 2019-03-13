/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 13.03.19 15:12.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.clear_8;

import org.apache.commons.cli.*;

final class Arguments {

    final private String applicationName;

    final private String[] arguments;
    final private Options options;
    final private CommandLineParser parser;

    private CommandLine commandLine;

    Arguments(String[] arguments, String applicationName) {
        this.arguments = arguments;
        this.applicationName = applicationName;

        parser = new DefaultParser();
        options = new Options();
    }

    boolean asBoolean(String option) {
        createCommandLineOnDemand();

        return Boolean.parseBoolean(commandLine.getOptionValue(option));
    }

    boolean asBoolean(String option, boolean defaultValue) {
        createCommandLineOnDemand();

        if (commandLine.hasOption(option)) {
            return Boolean.parseBoolean(commandLine.getOptionValue(option));
        }

        return defaultValue;
    }

    int asInteger(String option, int defaultValue) {
        createCommandLineOnDemand();

        if (commandLine.hasOption(option)) {
            return Integer.parseInt(commandLine.getOptionValue(option));
        }

        return defaultValue;
    }

    int asInteger(String option) {
        createCommandLineOnDemand();

        return Integer.parseInt(commandLine.getOptionValue(option));
    }

    String asString(String option, String defaultValue) {
        createCommandLineOnDemand();

        if (commandLine.hasOption(option)) {
            return commandLine.getOptionValue(option);
        }

        return defaultValue;
    }

    String asString(String option) {
        createCommandLineOnDemand();

        return commandLine.getOptionValue(option);
    }

    boolean given(String argumentName) {
        createCommandLineOnDemand();

        return commandLine.hasOption(argumentName);
    }

    void expect(String name,
                String longName,
                String description,
                Class<?> type,
                boolean hasArgument) {
        Option option = Option
                .builder(name)
                .longOpt(longName)
                .desc(description)
                .type(type)
                .required(false)
                .hasArg(hasArgument)
                .build();

        options.addOption(option);
    }

    void showHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(applicationName, options);
    }

    private void createCommandLineOnDemand() {
        if (commandLine == null) {
            try {
                commandLine = parser.parse(options, arguments);
            } catch (ParseException e) {
                showHelp();
                System.exit(1);
            }
        }
    }

}