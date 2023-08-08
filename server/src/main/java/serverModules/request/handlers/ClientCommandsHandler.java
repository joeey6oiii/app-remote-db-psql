package serverModules.request.handlers;

import commands.CommandDescription;
import commandsModule.commandsManagement.CommandRegistry;
import response.responses.ClientCommandsResponse;
import userModules.users.User;
import serverModules.connection.ConnectionModule;
import serverModules.request.data.ClientRequestInfo;
import serverModules.response.sender.ClientCommandsResponseSender;

import java.util.List;

/**
 * A class that works with the client commands request.
 */
public class ClientCommandsHandler implements RequestHandler {

    /**
     * A method that handles the client commands request and sends the client commands response.
     *
     * @param info information about the request
     */
    @Override
    public void handleRequest(ClientRequestInfo info) {
        ConnectionModule connectionModule = info.getConnectionModule();
        User client = info.getRequestOrigin();
        CommandRegistry commandRegistry = new CommandRegistry();
        List<CommandDescription> commands = commandRegistry.getCommands();
        ClientCommandsResponse commandsResponse = new ClientCommandsResponse(commands);

        new ClientCommandsResponseSender().sendResponse(connectionModule, client, commandsResponse);
    }
}