package serverModules.request.data;

import requests.Request;
import userModules.users.User;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents the information associated with a client request on the server.
 * Contains information about the user, the client request, and the server core.
 */
public class ClientRequestInfo {
    private final ConnectionModule connectionModule;
    private final User user;
    private final Request request;

    /**
     * A constructor for the client request information.
     *
     * @param connectionModule The server core
     * @param user The client
     * @param request The client request
     */
    public ClientRequestInfo(ConnectionModule connectionModule, User user, Request request) {
        this.connectionModule = connectionModule;
        this.user = user;
        this.request = request;
    }

    /**
     * @return the current server core.
     */
    public ConnectionModule getConnectionModule() {
        return connectionModule;
    }

    /**
     * @return the current client.
     */
    public User getRequestOrigin() {
        return user;
    }

    /**
     * @return the current client request.
     */
    public Request getRequest() {
        return request;
    }
}