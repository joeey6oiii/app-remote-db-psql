package serverModules.context;

import requests.Request;
import serverModules.request.data.RequestOrigin;
import serverModules.connection.ConnectionModule;

/**
 * A class that represents server settings. Contains server core, client and client request.
 */

public class ServerContext {
    private final ConnectionModule connectionModule;
    private final RequestOrigin requestOrigin;
    private final Request request;

    /**
     * A constructor for a server context.
     *
     * @param connectionModule server core
     * @param requestOrigin client
     * @param request client request
     */

    public ServerContext(ConnectionModule connectionModule, RequestOrigin requestOrigin, Request request) {
        this.connectionModule = connectionModule;
        this.requestOrigin = requestOrigin;
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

    public RequestOrigin getCallerBack() {
        return requestOrigin;
    }

    /**
     * A method that returns the current client request.
     */

    public Request getRequest() {
        return request;
    }

}





