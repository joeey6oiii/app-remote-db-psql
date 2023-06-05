package clientModules.response.handlers;

import response.responses.CommandExecutionResponse;

/**
 * A class that works with the "exit" command execution result response.
 */

public class ExitCommandHandler implements ResponseHandler<CommandExecutionResponse> {

    /**
     * A method that handles the "exit" command execution result response and ends the program.
     *
     * @param response the received response
     */

    @Override
    public void handleResponse(CommandExecutionResponse response) {
        System.out.println("Shutdown...");
        System.exit(0);
    }

}
