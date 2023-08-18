package commandsModule.commandsManagement;

import commands.CommandDescription;
import commandsModule.commands.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.CommandExecutionRequest;
import response.responses.AuthorizationResponse;
import response.responses.CommandExecutionResponse;
import response.responses.Response;
import serverModules.response.sender.ResponseAble;
import serverModules.response.sender.ResponseSender;
import token.Token;
import userModules.AuthenticatedUserRegistry;
import userModules.users.AuthenticatedUser;
import userModules.users.User;
import serverModules.connection.ConnectionModule;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A class that contains and executes commands.
 */
public class UserCommandHandler implements CommandHandler<CommandExecutionRequest> {
    private static final Logger logger = LogManager.getLogger("logger.UserCommandHandler");
    private final Map<String, BaseCommand> commands;
    private final ConnectionModule connectionModule;
    private final User user;

    /**
     * A constructor for a UserCommandHandler.
     * Creates a command collection and fills it with the available commands.
     */
    public UserCommandHandler(ConnectionModule connectionModule, User user) {
        commands = new LinkedHashMap<>();
        this.connectionModule = connectionModule;
        this.user = user;

        commands.put("add", new AddCommand());
        commands.put("info", new InfoCommand());
        commands.put("show", new ShowCommand());
        commands.put("clear", new ClearCommand());
        commands.put("remove_by_id", new RemoveByIdCommand());
        commands.put("help", new HelpCommand(this.commands));
        commands.put("exit", new ExitCommand());
        commands.put("update_by_id", new UpdateByIdCommand());
        commands.put("history", new HistoryCommand());
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
     * @param request the request, received from the client
     */
    @Override
    public void executeCommand(CommandExecutionRequest request) {
        String response;
        ResponseAble<Response> responseSender = new ResponseSender(connectionModule);
        boolean authorized = true;

        try {
            AuthenticatedUserRegistry userRegistry = AuthenticatedUserRegistry.getInstance();

            Token<?> token = request.getToken();
            AuthenticatedUser authenticatedUser = userRegistry.getAuthenticatedUser(token);

            if (authenticatedUser == null || authenticatedUser.getId() == null || token == null || token.getTokenValue() == null) {
                response = "You don't have permission to execute commands on the server";
                logger.error("Unauthorized user got access to command execution process");
                authorized = false;
            } else if (authenticatedUser.getSession().isSessionExpired(LocalDateTime.now())) {
                response = "Your session has expired. Please, log in again";
                logger.info("User session has expired while command execution process");
                authorized = false;

                AuthenticatedUserRegistry.getInstance().removeAuthenticatedUser(token);
            } else {
                CommandDescription simplifiedCommand = request.getDescriptionCommand();
                BaseCommand command = this.getCommandByDescription(simplifiedCommand);

                if (command.getClass().isAssignableFrom(CallerIdCommand.class)) {
                    CallerIdCommand callerIdCommand = (CallerIdCommand) command;
                    callerIdCommand.setCallerId(authenticatedUser.getId());
                }

                if (command.getClass().isAssignableFrom(ParameterizedCommand.class)) {
                    ParameterizedCommand parameterizedCommand = (ParameterizedCommand) command;
                    parameterizedCommand.setArguments(request.getArgs());
                    parameterizedCommand.execute();
                    response = parameterizedCommand.getResponse();
                } else {
                    command.execute();
                    response = command.getResponse();
                }

                authenticatedUser.addCommandToHistory(command);
            }
        } catch (IllegalArgumentException e) {
            response = "Command has invalid argument(s)";
            logger.error(response, e);
        } catch (IndexOutOfBoundsException e) {
            response = "Command has invalid number of arguments";
            logger.error(response, e);
        } catch (IOException e) {
            response = "Something went wrong during I/O operations";
            logger.error(response, e);
        } catch (Exception e) {
            response = "Unexpected error happened during command execution";
            logger.error(response, e);
        }

        if (authorized) {
            responseSender.sendResponse(user, new CommandExecutionResponse(response));
        } else {
            responseSender.sendResponse(user, new AuthorizationResponse(false, null, response));
        }
    }
}