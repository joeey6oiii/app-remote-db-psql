package serverModules.response.sender;

import response.responses.CommandExecutionResponse;
import response.responses.Response;
import serverModules.request.data.RequestOrigin;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents the client command execution response sender.
 */

public class ExecutionResultResponseSender implements ResponseAble<CommandExecutionResponse> {

    /**
     * A method that calls {@link ResponseSender#sendResponse(ConnectionModule, RequestOrigin, Response)} method.
     *
     * @param connectionModule server core
     * @param requestOrigin client
     * @param executionResponse answer to the client
     */

    @Override
    public void sendResponse(ConnectionModule connectionModule, RequestOrigin requestOrigin, CommandExecutionResponse executionResponse) {
        new ResponseSender().sendResponse(connectionModule, requestOrigin, executionResponse);
    }

}
