package clientModules.response.handlers;

import commands.CommandDescription;
import commandsModule.commands.CommandRegistry;
import response.responses.ClientCommandsResponse;

import java.util.List;

/**
 * A class that works with the commands' response.
 */
public class ClientCommandsHandler implements ResponseHandler<ClientCommandsResponse> {

    /**
     * A method that handles the commands' response and calls the
     * {@link CommandRegistry#setCommands(List)} method.
     *
     * @param response the received response
     */
    @Override
    public boolean handleResponse(ClientCommandsResponse response) {
        List<CommandDescription> commands = response.getCommands();
        CommandRegistry.setCommands(commands);

        return commands != null && !CommandRegistry.getCommands().isEmpty();
    }
}