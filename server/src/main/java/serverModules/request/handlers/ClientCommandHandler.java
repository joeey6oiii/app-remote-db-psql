package serverModules.request.handlers;

import commandsModule.commandsManagement.CommandHandler;
import requests.CommandExecutionRequest;
import userModules.users.User;
import serverModules.connection.ConnectionModule;
import serverModules.request.data.ClientRequestInfo;

/**
 * A class that works with the client command execution request.
 */
public class ClientCommandHandler implements RequestHandler {

    /**
     * A method that handles the client command execution request and calls the
     * {@link CommandHandler#execute(ConnectionModule, User, CommandExecutionRequest)} method.
     *
     * @param info information about the request
     */
    @Override
    public void handleRequest(ClientRequestInfo info) {
        ConnectionModule connectionModule = info.getConnectionModule();
        User client = info.getRequestOrigin();
        CommandExecutionRequest request = (CommandExecutionRequest) info.getRequest();

        new CommandHandler().execute(connectionModule, client, request);
    }
}