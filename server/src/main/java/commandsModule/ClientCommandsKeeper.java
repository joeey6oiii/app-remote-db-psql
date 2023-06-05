package commandsModule;

import commands.CommandDescription;
import commands.CommandType;

import java.util.ArrayList;
import java.util.List;

/**
 * A class where the {@link CommandDescription} objects are stored.
 */

public class ClientCommandsKeeper {
    private static final List<CommandDescription> commands;

    static {
        commands = new ArrayList<>();

        commands.add(new CommandDescription("add", CommandType.PERSON_SINGLE_ARGUMENT));
        commands.add(new CommandDescription("info", CommandType.ARGUMENTLESS));
        commands.add(new CommandDescription("show", CommandType.ARGUMENTLESS));
        commands.add(new CommandDescription("clear", CommandType.ARGUMENTLESS));
        commands.add(new CommandDescription("remove_by_id", CommandType.ARGUMENTLESS));
        commands.add(new CommandDescription("help", CommandType.ARGUMENTLESS));
        commands.add(new CommandDescription("exit", CommandType.EXIT));
        commands.add(new CommandDescription("update_by_id", CommandType.PERSON_SINGLE_ARGUMENT));
        commands.add(new CommandDescription("history", CommandType.ARGUMENTLESS));
        commands.add(new CommandDescription("sum_of_height", CommandType.ARGUMENTLESS));
        commands.add(new CommandDescription("average_of_height", CommandType.ARGUMENTLESS));
        commands.add(new CommandDescription("print_field_descending_birthday", CommandType.ARGUMENTLESS));
        commands.add(new CommandDescription("execute_script", CommandType.EXECUTE_SCRIPT));
        commands.add(new CommandDescription("remove_greater", CommandType.PERSON_SINGLE_ARGUMENT));
        commands.add(new CommandDescription("remove_lower", CommandType.PERSON_SINGLE_ARGUMENT));
    }

    /**
     * A method thar returns <code>List</code> with {@link CommandDescription} objects.
     */

    public static List<CommandDescription> getCommands() {
        return commands;
    }
}
