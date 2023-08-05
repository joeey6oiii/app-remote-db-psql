package commandsModule.commandsManagement;

import commands.CommandDescription;
import commands.CommandType;

import java.util.ArrayList;
import java.util.List;

/**
 * A registry that contains the commands known by the server.
 */
public class CommandRegistry {
    private final List<CommandDescription> commands;

    public CommandRegistry() {
        commands = new ArrayList<>();

        commands.add(new CommandDescription("add", CommandType.SINGLE_OBJECT_ARGUMENT));
        commands.add(new CommandDescription("info", CommandType.NO_ARGUMENT));
        commands.add(new CommandDescription("show", CommandType.NO_ARGUMENT));
        commands.add(new CommandDescription("clear", CommandType.NO_ARGUMENT));
        commands.add(new CommandDescription("remove_by_id", CommandType.NO_ARGUMENT));
        commands.add(new CommandDescription("help", CommandType.NO_ARGUMENT));
        commands.add(new CommandDescription("exit", CommandType.EXIT_SECTION));
        commands.add(new CommandDescription("update_by_id", CommandType.SINGLE_OBJECT_ARGUMENT));
        commands.add(new CommandDescription("history", CommandType.NO_ARGUMENT));
        commands.add(new CommandDescription("sum_of_height", CommandType.NO_ARGUMENT));
        commands.add(new CommandDescription("average_of_height", CommandType.NO_ARGUMENT));
        commands.add(new CommandDescription("print_field_descending_birthday", CommandType.NO_ARGUMENT));
        commands.add(new CommandDescription("execute_script", CommandType.SCRIPT_EXECUTION));
        commands.add(new CommandDescription("remove_greater", CommandType.SINGLE_OBJECT_ARGUMENT));
        commands.add(new CommandDescription("remove_lower", CommandType.SINGLE_OBJECT_ARGUMENT));
    }

    /**
     * @return <code>List</code> with {@link CommandDescription} objects.
     */
    public List<CommandDescription> getCommands() {
        return this.commands;
    }
}
