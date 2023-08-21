package serverModules.request.handlers;

import serverModules.request.data.RequestInfo;

/**
 * An interface for all request handler-implementers.
 */
public interface RequestHandler {

    /**
     * A method that handles the received request.
     *
     * @param info information about the request
     */
    void handleRequest(RequestInfo info);
}