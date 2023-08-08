package commands;

/**
 * An enum which contains possible types of commands for {@link CommandDescription} objects.
 *
 * {@link CommandType#NO_ARGUMENT} - Represents commands that do not require any arguments to execute
 * {@link CommandType#SINGLE_OBJECT_ARGUMENT} - Represents commands that require a single object as an argument
 * {@link CommandType#EXIT_SECTION} - Represents the command to exit the program or a particular section
 * {@link CommandType#SCRIPT_EXECUTION} - Represents commands that involve executing a script or a series of commands
 */
public enum CommandType {
    NO_ARGUMENT,
    SINGLE_OBJECT_ARGUMENT,
    EXIT_SECTION,
    SCRIPT_EXECUTION;
}
