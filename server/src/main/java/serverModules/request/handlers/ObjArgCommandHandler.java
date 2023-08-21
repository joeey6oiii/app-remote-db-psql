package serverModules.request.handlers;

import commandsModule.commands.ObjectArgumentCommand;
import commandsModule.commandsManagement.UserCommandHandler;
import requests.CommandExecutionRequest;
import requests.ObjectArgumentCommandExecutionRequest;
import serverModules.connection.ConnectionModule;
import serverModules.request.data.RequestInfo;

/**
 * A class that works with the client single argument command execution request.
 *
 * @param <T> the specified argument
 */
public class ObjArgCommandHandler<T> implements RequestHandler {
    private final ConnectionModule connectionModule;

    public ObjArgCommandHandler(ConnectionModule connectionModule) {
        this.connectionModule = connectionModule;
    }

    /**
     * A method that handles the client single argument command execution request and calls the
     * {@link UserCommandHandler#executeCommand(CommandExecutionRequest)} method.
     *
     * @param info information about the request
     */
    @Override
    public void handleRequest(RequestInfo info) {
        UserCommandHandler userCommandHandler = new UserCommandHandler(connectionModule, info.getRequesterUser());

        ObjectArgumentCommandExecutionRequest<T> executionRequest = (ObjectArgumentCommandExecutionRequest<T>) info.getRequest();

        ObjectArgumentCommand<T> command = (ObjectArgumentCommand<T>) userCommandHandler.getCommandByDescription(executionRequest.getDescriptionCommand());
        command.setObjArgument(executionRequest.getObjArg());

        userCommandHandler.executeCommand(executionRequest);
    }
}