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
        if (response == null) {
            return false;
        }

        List<CommandDescription> commands = response.getCommands();
        if (commands == null || commands.isEmpty()) {
            return false;
        }

        CommandRegistry.setCommands(commands);

        return true;
    }
}