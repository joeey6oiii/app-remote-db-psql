package commandsModule.commandsManagement;

import clientModules.connection.DataTransferConnectionModule;
import commands.CommandDescription;
import commandsModule.commands.AuthCommand;
import commandsModule.commands.CLSCommand;
import commandsModule.commands.ClientHelpCommand;
import commandsModule.commands.OfflineCommand;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class that handles the simplified commands.
 */
public class CommandHandler {
    private static Map<String, CommandDescription> commandsMap;
    private static Map<CommandDescription, String[]> missedCommandsMap;
    private final Map<String, OfflineCommand> offlineCommandsMap;
    private final CommandManager commandManager;
    private final Scanner scanner;

    /**
     * A constructor for command handler with map commands.
     *
     * @param commandsMap simplified commands map
     * @param scanner tool to scan input from the console
     * @param dataTransferConnectionModule client core
     */
    public CommandHandler(Map<String, CommandDescription> commandsMap, Scanner scanner, DataTransferConnectionModule dataTransferConnectionModule) {
        CommandHandler.commandsMap = commandsMap;
        missedCommandsMap = new LinkedHashMap<>();
        this.scanner = scanner;
        commandManager = new CommandManager(dataTransferConnectionModule);

        offlineCommandsMap = new LinkedHashMap<>();
        offlineCommandsMap.put("auth", new AuthCommand(dataTransferConnectionModule));
        offlineCommandsMap.put("cls", new CLSCommand());
        offlineCommandsMap.put("c_help", new ClientHelpCommand(offlineCommandsMap));
    }

    /**
     * A constructor for command handler with list commands. List automatically converts to a map.
     *
     * @param commandsMap simplified commands list
     * @param scanner tool to scan input from the console
     * @param dataTransferConnectionModule client core
     */
    public CommandHandler(List<CommandDescription> commandsMap, Scanner scanner, DataTransferConnectionModule dataTransferConnectionModule) {
        CommandHandler.commandsMap = commandsMap.stream().collect(Collectors.toMap(CommandDescription::getCommandName, Function.identity()));
        missedCommandsMap = new LinkedHashMap<>();
        this.scanner = scanner;
        commandManager = new CommandManager(dataTransferConnectionModule);

        offlineCommandsMap = new LinkedHashMap<>();
        offlineCommandsMap.put("auth", new AuthCommand(dataTransferConnectionModule));
        offlineCommandsMap.put("cls", new CLSCommand());
        offlineCommandsMap.put("c_help", new ClientHelpCommand(offlineCommandsMap));
    }

    /**
     * A method that returns {@link CommandDescription} by the specified name from the commands' collection.
     *
     * @param name simplified command name
     */
    public static CommandDescription getCommandFromName(String name) {
        if (commandsMap != null) {
            return commandsMap.get(name);
        }
        return null;
    }

    /**
     * A method that returns missed commands' collection.
     * Missed commands are commands that were not executed on server due to some problems (Ex: Server was unavailable)
     */
    public static Map<CommandDescription, String[]> getMissedCommandsMap() {
        if (missedCommandsMap == null) {
            return new LinkedHashMap<>();
        }
        return missedCommandsMap;
    }

    /**
     * A method that manages the simplified commands by handling input or missed commands collection.
     * Uses {@link CommandManager#manageCommand(CommandDescription, String[])} to
     * continue operations connected to sending and receiving.
     */
    public void startHandlingInput() {
        String consoleInput;
        while (true) {
            if (!missedCommandsMap.isEmpty()) {
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
            
            CommandDescription command = commandsMap.get(tokens[0]);
            if (command == null) {
                OfflineCommand offlineCommand = offlineCommandsMap.get(tokens[0]);
                if (offlineCommand == null) {
                    System.out.println("Not Recognized as an Internal or External Command. Type \"help\" or \"c_help\" to see available commands");
                } else {
                    offlineCommand.execute();
                }
                continue;
            }

            if (!missedCommandsMap.isEmpty()) {
                missedCommandsMap.put(command, tokens);
                System.out.println("Added command to the end of the missed commands collection due to its not emptiness");
            } else {
                commandManager.manageCommand(command, tokens);
                continue;
            }

            if (!missedCommandsMap.isEmpty()) {
                System.out.println("Trying to send commands from missed commands collection...");
                Map<CommandDescription, String[]> copyOfMissedCommands = new LinkedHashMap<>(missedCommandsMap);
                copyOfMissedCommands.forEach(commandManager::manageCommand);
            }
        }
    }
}