/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 24.02.19 03:28.
 * Copyright (c) 2019. All rights reserved.
 */

import org.apache.commons.cli.*;

class Arguments {

    private String[] arguments;
    private CommandLine commandLine;
    private String executableName;
    private Options options;
    private CommandLineParser parser;

    Arguments(String[] arguments, String executableName) {
        this.arguments = arguments;
        this.executableName = executableName;

        parser = new DefaultParser();
        options = new Options();
    }

    void expect(Class<?> type, boolean required, String name, String longName,
            String description) {
        Option option = Option.builder(name).longOpt(longName).desc(description)
                .type(type).hasArg().required(required).build();

        options.addOption(option);
    }

    int toInteger(String option, int defaultValue) {
        if (commandLine == null) {
            createCommandLine();
        }

        if (commandLine.hasOption(option)) {
            return Integer.parseInt(commandLine.getOptionValue(option));
        }

        return defaultValue;
    }

    int toInteger(String option) {
        if (commandLine == null) {
            createCommandLine();
        }

        return Integer.parseInt(commandLine.getOptionValue(option));
    }

    String toString(String option, String defaultValue) {
        if (commandLine == null) {
            createCommandLine();
        }

        if (commandLine.hasOption(option)) {
            commandLine.getOptionValue(option);
        }

        return defaultValue;
    }

    String toString(String option) {
        if (commandLine == null) {
            createCommandLine();
        }

        return commandLine.getOptionValue(option);
    }

    private void createCommandLine() {
        try {
            commandLine = parser.parse(options, arguments);
        } catch (ParseException e) {
            showUsageMessage(e.getMessage());
            System.exit(1);
        }
    }

    private void showUsageMessage(String message) {
        System.err.println(message);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(executableName, options);
    }
}