package commandsModule.handler;

import commands.CommandDescription;
import commandsModule.commands.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.CommandExecutionRequest;
import response.responses.CommandExecutionResponse;
import serverModules.callerBack.CallerBack;
import serverModules.connection.ConnectionModule;
import serverModules.response.sender.ExecutionResultResponseSender;

import java.io.IOException;
import java.util.*;

/**
 * A class that contains and executes commands.
 */

public class CommandHandler {
    private static final Logger logger = LogManager.getLogger("logger.CommandHandler");
    private final Map<String, BaseCommand> commands;
    private static List<BaseCommand> history;

    /**
     * A constructor for a CommandHandler.
     * Creates a command collection and fills it with the available commands.
     * Creates a history list where executed commands will be stored.
     */

    public CommandHandler() {
        commands = new LinkedHashMap<>();
        history = getHistory();

        commands.put("add", new AddCommand());
        commands.put("info", new InfoCommand());
        commands.put("show", new ShowCommand());
        commands.put("clear", new ClearCommand());
        commands.put("remove_by_id", new RemoveByIdCommand());
        commands.put("help", new HelpCommand(this.commands));
        commands.put("exit", new ExitCommand());
        commands.put("update_by_id", new UpdateByIdCommand());
        commands.put("history", new HistoryCommand(this.commands));
        commands.put("sum_of_height", new SumOfHeightCommand());
        commands.put("average_of_height", new AverageOfHeightCommand());
        commands.put("print_field_descending_birthday", new PrintFieldDescendingBirthdayCommand());
        commands.put("execute_script", new ExecuteScriptCommand());
        commands.put("remove_greater", new RemoveGreaterCommand());
        commands.put("remove_lower", new RemoveLowerCommand());
    }

    /**
     * A method for getting a list containing available commands.
     *
     * @return returns a list of commands
     */

    public Map<String, BaseCommand> getCommands() {
        return commands;
    }

    /**
     * A method for getting command execution history.
     *
     * @return history of used commands
     */

    public static List<BaseCommand> getHistory() {
        if (history == null) {
            history = new ArrayList<>();
        }
        return history;
    }

    /**
     * A method that returns {@link BaseCommand} inheritor by the specified {@link CommandDescription}.
     *
     * @param description a command description, by which the relevant command will be selected
     */

    public BaseCommand getCommandByDescription(CommandDescription description)  {
        return commands.get(description.getCommandName().toLowerCase());
    }

    /**
     * When called, checks if the received command is parametrized or not, then, according to check result, executes the
     * command with or without arguments. After, gets response from the command and adds command to the command history
     * list, then sends the response to the client.
     *
     * @param connectionModule server core. Used to send data
     * @param callerBack the client to whom to send response
     * @param request the request, received from the client
     */

    public void execute(ConnectionModule connectionModule, CallerBack callerBack, CommandExecutionRequest request) {
        String response;
        try {
            CommandDescription simplifiedCommand = request.getDescriptionCommand();
            BaseCommand command = this.getCommandByDescription(simplifiedCommand);

            if (command instanceof ParameterizedCommand parameterizedCommand) {
                parameterizedCommand.setArguments(request.getArgs());
                parameterizedCommand.execute();
                response = parameterizedCommand.getResponse();
            } else {
                command.execute();
                response = command.getResponse();
            }

            history.add(command);
        } catch (IllegalArgumentException | NullPointerException e) {
            response = "Command has invalid argument(s)";
            logger.fatal(response, e);
        } catch (IndexOutOfBoundsException e) {
            response = "Command has invalid number of arguments";
            logger.fatal(response, e);
        } catch (IOException e) {
            response = "Something went wrong during I/O operations";
            logger.fatal(response, e);
        } catch (Exception e) {
            response = "Unexpected error happened during command execution";
            logger.fatal(response, e);
        }

        new ExecutionResultResponseSender().sendResponse(connectionModule, callerBack, new CommandExecutionResponse(response));
    }

}