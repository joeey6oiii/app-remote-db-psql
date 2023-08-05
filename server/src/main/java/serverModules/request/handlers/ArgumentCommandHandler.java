package serverModules.request.handlers;

import commandsModule.commands.ObjectArgumentCommand;
import commandsModule.commandsManagement.CommandHandler;
import requests.CommandExecutionRequest;
import requests.SingleArgumentCommandExecutionRequest;
import userModules.users.User;
import serverModules.connection.ConnectionModule;
import serverModules.context.ServerContext;

/**
 * A class that works with the client single argument command execution request.
 *
 * @param <T> the specified argument
 */
public class ArgumentCommandHandler<T> implements RequestHandler {

    /**
     * A method that handles the client single argument command execution request and calls the
     * {@link CommandHandler#execute(ConnectionModule, User, CommandExecutionRequest)} method.
     *
     * @param context the specified server settings
     */
    @Override
    public void handleRequest(ServerContext context) {
        CommandHandler commandHandler = new CommandHandler();

        SingleArgumentCommandExecutionRequest<T> executionRequest = (SingleArgumentCommandExecutionRequest<T>) context.getRequest();

        ObjectArgumentCommand<T> command = (ObjectArgumentCommand<T>) commandHandler.getCommandByDescription(executionRequest.getDescriptionCommand());
        command.setObjArgument(executionRequest.getArg());

        commandHandler.execute(context.getConnectionModule(), context.getRequestOrigin(), executionRequest);
    }
}