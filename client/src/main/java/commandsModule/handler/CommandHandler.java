package commandsModule.handler;

import clientModules.connection.DataTransferConnectionModule;
import commands.CommandDescription;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class that handles the simplified commands.
 */

public class CommandHandler {
    private static Map<String, CommandDescription> commands;
    private static Map<CommandDescription, String[]> missedCommands;
    private final CommandManager commandManager;
    private final DataTransferConnectionModule module;
    private final Scanner scanner;

    /**
     * A constructor for command handler with map commands.
     *
     * @param commands simplified commands map
     * @param scanner tool to scan input from the console
     * @param module client core
     */

    public CommandHandler(Map<String, CommandDescription> commands, Scanner scanner, DataTransferConnectionModule module) {
        CommandHandler.commands = commands;
        missedCommands = new LinkedHashMap<>();
        this.scanner = scanner;
        this.module = module;
        commandManager = new CommandManager();
    }

    /**
     * A constructor for command handler with list commands. List automatically converts to a map.
     *
     * @param commands simplified commands list
     * @param scanner tool to scan input from the console
     * @param module client core
     */

    public CommandHandler(List<CommandDescription> commands, Scanner scanner, DataTransferConnectionModule module) {
        CommandHandler.commands = commands.stream().collect(Collectors.toMap(CommandDescription::getCommandName, Function.identity()));
        missedCommands = new LinkedHashMap<>();
        this.scanner = scanner;
        this.module = module;
        commandManager = new CommandManager();
    }

    /**
     * A method that returns {@link CommandDescription} by the specified name from the commands' collection.
     *
     * @param name simplified command name
     */

    public static CommandDescription getCommandByName(String name) {
        if (commands != null) {
            return commands.get(name);
        }
        return null;
    }

    /**
     * A method that returns missed commands' collection.
     * Missed commands are commands that were not executed on server due to some problems (Ex: Server was unavailable)
     */

    public static Map<CommandDescription, String[]> getMissedCommands() {
        if (missedCommands != null) {
            return missedCommands;
        }
        missedCommands = new LinkedHashMap<>();
        return missedCommands;
    }

    /**
     * A method that manages the simplified commands by handling input or missed commands collection.
     * Uses {@link CommandManager#manageCommand(CommandDescription, String[], DataTransferConnectionModule)} to
     * continue operations connected to sending and receiving.
     */

    public void startHandlingInput() {
        String consoleInput;
        while (true) {
            if (!missedCommands.isEmpty()) {
                System.out.println("Server failed to execute some commands (perhaps the" +
                        " server is or was unavailable). Returning to the console input");
            }

            System.out.print("$ ");

            if (!scanner.hasNextLine()) {
                System.out.println("Scanner was closed. Unable to continue handling input");
                break;
            }

            consoleInput = scanner.nextLine().trim();
            if (consoleInput.isEmpty()) { continue; }
            String[] tokens = consoleInput.toLowerCase().split(" ");
            CommandDescription command = commands.get(tokens[0]);
            if (command == null) {
                System.out.println("Not Recognized as an Internal or External Command");
                continue;
            }
            if (!missedCommands.isEmpty()) {
                missedCommands.put(command, tokens);
                System.out.println("Added command to the end of the missed commands collection due to its not emptiness");
            } else {
                commandManager.manageCommand(command, tokens, module);
                continue;
            }

            if (!missedCommands.isEmpty()) {
                System.out.println("Trying to send commands from missed commands collection...");
                Map<CommandDescription, String[]> copyOfMissedCommands = new LinkedHashMap<>(missedCommands);
                copyOfMissedCommands.forEach((key, value) -> commandManager.manageCommand(key, value, module));
            }
        }
    }

}
