package serverModules.context;

import requests.Request;
import serverModules.callerBack.CallerBack;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents server settings. Contains server core, client and client request.
 */

public class ServerContext {
    private final ConnectionModule connectionModule;
    private final CallerBack callerBack;
    private final Request request;

    /**
     * A constructor for a server context.
     *
     * @param connectionModule server core
     * @param callerBack client
     * @param request client request
     */

    public ServerContext(ConnectionModule connectionModule, CallerBack callerBack, Request request) {
        this.connectionModule = connectionModule;
        this.callerBack = callerBack;
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

    public CallerBack getCallerBack() {
        return callerBack;
    }

    /**
     * A method that returns the current client request.
     */

    public Request getRequest() {
        return request;
    }

}





