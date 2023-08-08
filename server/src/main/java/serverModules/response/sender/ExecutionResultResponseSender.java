package serverModules.response.sender;

import response.responses.CommandExecutionResponse;
import response.responses.Response;
import userModules.users.User;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents the client command execution response sender.
 */
public class ExecutionResultResponseSender implements ResponseAble<CommandExecutionResponse> {

    /**
     * A method that calls {@link ResponseSender#sendResponse(ConnectionModule, User, Response)} method.
     *
     * @param connectionModule server core
     * @param user client
     * @param executionResponse answer to the client
     */
    @Override
    public void sendResponse(ConnectionModule connectionModule, User user, CommandExecutionResponse executionResponse) {
        new ResponseSender().sendResponse(connectionModule, user, executionResponse);
    }
}