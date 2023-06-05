package serverModules.response.sender;

import response.responses.CommandExecutionResponse;
import response.responses.Response;
import serverModules.callerBack.CallerBack;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents the client command execution response sender.
 */

public class ExecutionResultResponseSender implements ResponseAble<CommandExecutionResponse> {

    /**
     * A method that calls {@link ResponseSender#sendResponse(ConnectionModule, CallerBack, Response)} method.
     *
     * @param connectionModule server core
     * @param callerBack client
     * @param executionResponse answer to the client
     */

    @Override
    public void sendResponse(ConnectionModule connectionModule, CallerBack callerBack, CommandExecutionResponse executionResponse) {
        new ResponseSender().sendResponse(connectionModule, callerBack, executionResponse);
    }

}
