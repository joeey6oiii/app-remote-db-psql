package serverModules.request.data;

import requests.Request;
import userModules.users.AbstractUser;

/**
 * A class that represents the information associated with a client request on the server.
 * Contains information about the user, the client request, and the server core.
 */
public class ClientRequestInfo {
    private final AbstractUser user;
    private final Request request;

    /**
     * A constructor for the client request information.
     *
     * @param user The client
     * @param request The client request
     */
    public ClientRequestInfo(AbstractUser user, Request request) {
        this.user = user;
        this.request = request;
    }

    /**
     * @return the current client.
     */
    public AbstractUser getRequesterUser() {
        return this.user;
    }

    /**
     * @return the current client request.
     */
    public Request getRequest() {
        return this.request;
    }
}