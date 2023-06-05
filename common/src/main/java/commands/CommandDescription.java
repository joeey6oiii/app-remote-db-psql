package commands;

import java.io.Serializable;

/**
 * A class that represents a command in the simplified form. Simplified commands are used to work with the client requests.
 * The implementation allows, when adding a new command to the server, not to add it to the client.
 */

public class CommandDescription implements Serializable {
    private final String name;
    private final CommandType type;

    /**
     * A constructor of simplified command.
     *
     * @param commandName the name of the original command
     * @param type the {@link CommandType} of the original command
     */

    public CommandDescription(String commandName, CommandType type) {
        this.name = commandName;
        this.type = type;
    }

    /**
     * A method that returns the name of the simplified command.
     */

    public String getCommandName() {
        return name;
    }

    /**
     * A method that returns the {@link CommandType} of the simplified command.
     */

    public CommandType getType() {
        return this.type;
    }
}
