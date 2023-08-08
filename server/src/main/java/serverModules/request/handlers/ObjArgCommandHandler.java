package serverModules.request.handlers;

import commandsModule.commands.ObjectArgumentCommand;
import commandsModule.commandsManagement.CommandHandler;
import requests.CommandExecutionRequest;
import requests.ObjectArgumentCommandExecutionRequest;
import userModules.users.User;
import serverModules.connection.ConnectionModule;
import serverModules.request.data.ClientRequestInfo;

/**
 * A class that works with the client single argument command execution request.
 *
 * @param <T> the specified argument
 */
public class ObjArgCommandHandler<T> implements RequestHandler {

    /**
     * A method that handles the client single argument command execution request and calls the
     * {@link CommandHandler#execute(ConnectionModule, User, CommandExecutionRequest)} method.
     *
     * @param info information about the request
     */
    @Override
    public void handleRequest(ClientRequestInfo info) {
        CommandHandler commandHandler = new CommandHandler();

        ObjectArgumentCommandExecutionRequest<T> executionRequest = (ObjectArgumentCommandExecutionRequest<T>) info.getRequest();

        ObjectArgumentCommand<T> command = (ObjectArgumentCommand<T>) commandHandler.getCommandByDescription(executionRequest.getDescriptionCommand());
        command.setObjArgument(executionRequest.getObjArg());

        commandHandler.execute(info.getConnectionModule(), info.getRequestOrigin(), executionRequest);
    }
}