/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 12.03.19 12:37.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8;

import org.apache.commons.cli.*;

final class Arguments {

    final private String[] arguments;
    final private String executableName;
    final private Options options;
    final private CommandLineParser parser;
    private CommandLine commandLine;

    Arguments(String[] arguments, String applicationName) {
        this.arguments = arguments;
        this.executableName = applicationName;

        parser = new DefaultParser();
        options = new Options();
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
            commandLine.getOptionValue(option);
        }

        return defaultValue;
    }

    String asString(String option) {
        createCommandLineOnDemand();

        return commandLine.getOptionValue(option);
    }

    void expect(String name, String longName, String description, Class<?> type, boolean required) {
        Option option = Option
                .builder(name)
                .longOpt(longName)
                .desc(description)
                .type(type)
                .required(required)
                .hasArg()
                .build();

        options.addOption(option);
    }

    private void createCommandLineOnDemand() {
        if (commandLine == null) {
            try {
                commandLine = parser.parse(options, arguments);
            } catch (ParseException e) {
                showUsageMessage(e.getMessage());
                System.exit(1);
            }
        }
    }

    private void showUsageMessage(String message) {
        System.err.println(message);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(executableName, options);
    }

}