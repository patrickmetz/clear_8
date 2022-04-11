package de.patrickmetz.clear_8.cli;

/**
 * This facade hides the complexity of the Apache command line interface
 * parser subsystem and simplifies its usage. It basically delivers the
 * values of command line options, given by the user, or uses default
 * values, given by the programmer.
 */
public interface CommandLineFacade {
    void expectOption(
            String shortName,
            String longName,
            String helpText,
            Class<?> dataType,
            boolean hasArgument
    );

    boolean hasOption(String longOrShortName);

    String getOptionValue(String longOrShortName);

    int getOptionValueOrDefault(String longOrShortName, int defaultValue);

    boolean getOptionValueOrDefault(String longOrShortName, boolean defaultValue);

    void printExpectedOptions();
}
