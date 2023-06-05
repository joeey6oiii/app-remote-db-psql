package serverModules.request.handlers;

import serverModules.context.ServerContext;

/**
 * An interface for all request handler-implementers.
 */

public interface RequestHandler {

    /**
     * A method that handles the received request.
     *
     * @param context the specified server settings
     */

    void handleRequest(ServerContext context);
}
