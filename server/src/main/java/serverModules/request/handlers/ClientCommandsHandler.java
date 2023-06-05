package serverModules.request.handlers;

import commands.CommandDescription;
import commandsModule.ClientCommandsKeeper;
import response.responses.ClientCommandsResponse;
import serverModules.callerBack.CallerBack;
import serverModules.connection.ConnectionModule;
import serverModules.context.ServerContext;
import serverModules.response.sender.ClientCommandsResponseSender;

import java.util.List;

/**
 * A class that works with the client commands request.
 */

public class ClientCommandsHandler implements RequestHandler {

    /**
     * A method that handles the client commands request and sends the client commands response.
     *
     * @param context the specified server settings
     */

    @Override
    public void handleRequest(ServerContext context) {
        ConnectionModule connectionModule = context.getConnectionModule();
        CallerBack client = context.getCallerBack();
        List<CommandDescription> commands = ClientCommandsKeeper.getCommands();
        ClientCommandsResponse commandsResponse = new ClientCommandsResponse(commands);

        new ClientCommandsResponseSender().sendResponse(connectionModule, client, commandsResponse);
    }

}
