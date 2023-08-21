package serverModules.request.handlers;

import commands.CommandDescription;
import commandsModule.commandsManagement.CommandRegistry;
import response.responses.ClientCommandsResponse;
import serverModules.response.sender.ChunkedResponseSender;
import serverModules.response.sender.ResponseSender;
import userModules.users.AbstractUser;
import serverModules.connection.ConnectionModule;
import serverModules.request.data.RequestInfo;

import java.util.List;

/**
 * A class that works with the client commands request.
 */
public class ClientCommandsHandler implements RequestHandler {
    private final ResponseSender responseSender;

    public ClientCommandsHandler(ConnectionModule connectionModule) {
        this.responseSender = new ChunkedResponseSender(connectionModule);
    }

    /**
     * A method that handles the client commands request and sends the client commands response.
     *
     * @param info information about the request
     */
    @Override
    public void handleRequest(RequestInfo info) {
        AbstractUser client = info.getRequesterUser();

        CommandRegistry commandRegistry = new CommandRegistry();
        List<CommandDescription> commands = commandRegistry.getCommands();

        ClientCommandsResponse commandsResponse = new ClientCommandsResponse(commands);
        responseSender.sendResponse(client, commandsResponse);
    }
}