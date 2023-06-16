package serverModules.response.sender;

import response.responses.ClientCommandsResponse;
import response.responses.Response;
import userModules.users.User;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents the client commands response sender.
 */

public class ClientCommandsResponseSender implements ResponseAble<ClientCommandsResponse> {

    /**
     * A method that calls {@link ResponseSender#sendResponse(ConnectionModule, User, Response)} method.
     *
     * @param connectionModule server core
     * @param user client
     * @param commandsResponse answer to the client
     */

    @Override
    public void sendResponse(ConnectionModule connectionModule, User user, ClientCommandsResponse commandsResponse) {
        new ResponseSender().sendResponse(connectionModule, user, commandsResponse);
    }

}
