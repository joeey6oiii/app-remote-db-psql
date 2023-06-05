package serverModules.response.sender;

import response.responses.ClientCommandsResponse;
import response.responses.Response;
import serverModules.callerBack.CallerBack;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents the client commands response sender.
 */

public class ClientCommandsResponseSender implements ResponseAble<ClientCommandsResponse> {

    /**
     * A method that calls {@link ResponseSender#sendResponse(ConnectionModule, CallerBack, Response)} method.
     *
     * @param connectionModule server core
     * @param callerBack client
     * @param commandsResponse answer to the client
     */

    @Override
    public void sendResponse(ConnectionModule connectionModule, CallerBack callerBack, ClientCommandsResponse commandsResponse) {
        new ResponseSender().sendResponse(connectionModule, callerBack, commandsResponse);
    }

}
