package commandsModule.commands;

import commands.CommandDescription;

import java.util.List;

/**
 * A class where the {@link CommandDescription} objects are stored.
 */
public class CommandRegistry {
    private static List<CommandDescription> commands;

    /**
     * A method that sets the specified <code>List</code> of {@link CommandDescription} objects.
     *
     * @param commands the specified list with the {@link CommandDescription} objects
     */
    public static void setCommands(List<CommandDescription> commands) {
        CommandRegistry.commands = commands;
    }

    /**
     * A method thar returns <code>List</code> with {@link CommandDescription} objects.
     */
    public static List<CommandDescription> getCommands() {
        return commands;
    }
}
