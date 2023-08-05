package commands;

/**
 * An enum which contains possible types of commands for {@link CommandDescription} objects.
 */
public enum CommandType {
    NO_ARGUMENT,                     // Represents commands that do not require any arguments to execute.
    SINGLE_OBJECT_ARGUMENT,          // Represents commands that require a single object as an argument.
    EXIT_SECTION,                    // Represents the command to exit the program or a particular section.
    SCRIPT_EXECUTION;                // Represents commands that involve executing a script or a series of commands.
}