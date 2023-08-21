package serverModules.request.handlers;

import commandsModule.commandsManagement.UserCommandHandler;
import requests.CommandExecutionRequest;
import serverModules.connection.ConnectionModule;
import userModules.users.AbstractUser;
import serverModules.request.data.ClientRequestInfo;

/**
 * A class that works with the client command execution request.
 */
public class ClientCommandHandler implements RequestHandler {
    private final ConnectionModule connectionModule;

    public ClientCommandHandler(ConnectionModule connectionModule) {
        this.connectionModule = connectionModule;
    }

    /**
     * A method that handles the client command execution request and calls the
     * {@link UserCommandHandler#executeCommand(CommandExecutionRequest)} method.
     *
     * @param info information about the request
     */
    @Override
    public void handleRequest(ClientRequestInfo info) {
        AbstractUser client = info.getRequesterUser();
        CommandExecutionRequest request = (CommandExecutionRequest) info.getRequest();

        new UserCommandHandler(connectionModule, client).executeCommand(request);
    }
}