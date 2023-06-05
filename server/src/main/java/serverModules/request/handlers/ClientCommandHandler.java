package serverModules.request.handlers;

import commandsModule.handler.CommandHandler;
import requests.CommandExecutionRequest;
import serverModules.callerBack.CallerBack;
import serverModules.connection.ConnectionModule;
import serverModules.context.ServerContext;

/**
 * A class that works with the client command execution request.
 */

public class ClientCommandHandler implements RequestHandler {

    /**
     * A method that handles the client command execution request and calls the
     * {@link CommandHandler#execute(ConnectionModule, CallerBack, CommandExecutionRequest)} method.
     *
     * @param context the specified server settings
     */

    @Override
    public void handleRequest(ServerContext context) {
        ConnectionModule connectionModule = context.getConnectionModule();
        CallerBack client = context.getCallerBack();
        CommandExecutionRequest request = (CommandExecutionRequest) context.getRequest();

        new CommandHandler().execute(connectionModule, client, request);
    }
}