package clientModules.response.handlers;

import response.responses.CommandExecutionResponse;

/**
 * A class that works with the command execution result response.
 */

public class ExecutionResultHandler implements ResponseHandler<CommandExecutionResponse> {

    /**
     * A method that handles the command execution result response and outputs the result.
     *
     * @param response the received response
     */

    @Override
    public void handleResponse(CommandExecutionResponse response) {
        System.out.println(response.getResult());
    }

}
