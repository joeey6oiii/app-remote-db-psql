package commandsModule.commandsManagement;

import clientModules.connection.DataTransferConnectionModule;
import commands.CommandDescription;
import commands.CommandType;
import commandsModule.commandReceivers.*;
import exceptions.IllegalManagerArgumentException;
import outputService.ColoredPrintStream;
import outputService.MessageType;
import outputService.OutputSource;

import java.util.HashMap;
import java.util.Optional;

/**
 * A class that manages simplified commands.
 */
public class CommandManager {
    private final HashMap<CommandType, CommandReceiver> map;

    public CommandManager(DataTransferConnectionModule dataTransferConnectionModule) {
        this.map = new HashMap<>();

        map.put(CommandType.NO_ARGUMENT, new ExecutionResultReceiver(dataTransferConnectionModule));
        map.put(CommandType.SINGLE_OBJECT_ARGUMENT, new PersonCommandResultReceiver(dataTransferConnectionModule));
        map.put(CommandType.EXIT_SECTION, new ExitCommandReceiver(dataTransferConnectionModule));
        map.put(CommandType.SCRIPT_EXECUTION, new ScriptCommandReceiver(dataTransferConnectionModule));
    }

    /**
     * Finds a matching simplified command with a received simplified command and manages it using the
     * {@link CommandReceiver#receiveCommand(CommandDescription, String[])} method.
     * 
     * @param command simplified command
     * @param args simplified command arguments
     */
    public void manageCommand(CommandDescription command, String[] args) {
        try {
            Optional.ofNullable(map.get(command.getType())).orElseThrow(() ->
                    new IllegalManagerArgumentException("CommandManager contains illegal argument")).receiveCommand(command, args);
        } catch (IllegalManagerArgumentException e) {
            ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());
            cps.println(cps.formatMessage(MessageType.ERROR, "Failed to manage response"));
        }
    }
}