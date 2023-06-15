package serverModules.response.sender;

import response.responses.ClientCommandsResponse;
import response.responses.Response;
import serverModules.request.data.RequestOrigin;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents the client commands response sender.
 */

public class ClientCommandsResponseSender implements ResponseAble<ClientCommandsResponse> {

    /**
     * A method that calls {@link ResponseSender#sendResponse(ConnectionModule, RequestOrigin, Response)} method.
     *
     * @param connectionModule server core
     * @param requestOrigin client
     * @param commandsResponse answer to the client
     */

    @Override
    public void sendResponse(ConnectionModule connectionModule, RequestOrigin requestOrigin, ClientCommandsResponse commandsResponse) {
        new ResponseSender().sendResponse(connectionModule, requestOrigin, commandsResponse);
    }

}
