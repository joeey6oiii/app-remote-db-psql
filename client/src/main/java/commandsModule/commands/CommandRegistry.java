package commandsModule.commands;

import commands.CommandDescription;

import java.util.List;

/**
 * A class where the {@link CommandDescription} objects known by a server are stored.
 */
public class CommandRegistry {
    private static CommandRegistry singleInstance;
    private List<CommandDescription> commands;

    public static CommandRegistry getInstance() {
        if (singleInstance == null) {
            singleInstance = new CommandRegistry();
        }
        return singleInstance;
    }

    /**
     * A method that sets the specified <code>List</code> of {@link CommandDescription} objects.
     *
     * @param commands the specified list with the {@link CommandDescription} objects
     */
    public void setCommands(List<CommandDescription> commands) {
        this.commands = commands;
    }

    /**
     * A method thar returns <code>List</code> with {@link CommandDescription} objects.
     */
    public List<CommandDescription> getCommands() {
        return this.commands;
    }
}