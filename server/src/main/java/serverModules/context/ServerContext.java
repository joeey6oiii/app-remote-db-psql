package serverModules.context;

import requests.Request;
import userModules.passwordService.EncryptedPassword;
import userModules.users.RegisteredUser;
import userModules.users.RegisteredUserData;
import userModules.users.User;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents server settings. Contains server core, client and client request.
 */

public class ServerContext {
    private final ConnectionModule connectionModule;
    private final User user;
    private final Request request;

    /**
     * A constructor for a server context.
     *
     * @param connectionModule server core
     * @param user client
     * @param request client request
     */

    public ServerContext(ConnectionModule connectionModule, User user, Request request) {
        this.connectionModule = connectionModule;
        this.user = user;
        this.request = request;
    }

    /**
     * A method that returns the current server core.
     */

    public ConnectionModule getConnectionModule() {
        return connectionModule;
    }

    /**
     * A method that returns the current client.
     */

    public User getRequestOrigin() {
        return user;
    }

    /**
     * A method that returns the current client request.
     */

    public Request getRequest() {
        return request;
    }

}





