package serverModules.request.handlers;

import commandsModule.commandsManagement.CommandHandler;
import requests.CommandExecutionRequest;
import userModules.users.User;
import serverModules.connection.ConnectionModule;
import serverModules.context.ServerContext;

/**
 * A class that works with the client command execution request.
 */
public class ClientCommandHandler implements RequestHandler {

    /**
     * A method that handles the client command execution request and calls the
     * {@link CommandHandler#execute(ConnectionModule, User, CommandExecutionRequest)} method.
     *
     * @param context the specified server settings
     */
    @Override
    public void handleRequest(ServerContext context) {
        ConnectionModule connectionModule = context.getConnectionModule();
        User client = context.getRequestOrigin();
        CommandExecutionRequest request = (CommandExecutionRequest) context.getRequest();

        new CommandHandler().execute(connectionModule, client, request);
    }
}