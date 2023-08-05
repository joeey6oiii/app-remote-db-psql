package serverModules.request.handlers;

import exceptions.IllegalManagerArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.*;
import response.responses.ErrorResponse;
import serverModules.context.ServerContext;
import serverModules.request.handlers.authenticationHandlers.AuthorizationHandler;
import serverModules.request.handlers.authenticationHandlers.RegistrationHandler;
import serverModules.response.sender.ResponseSender;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * A class that manages the received requests.
 */
public class RequestHandlerManager {
    private static final Logger logger = LogManager.getLogger("logger.RequestHandlerManager");
    private final LinkedHashMap<Class<? extends Request>, RequestHandler> handlers;

    {
        handlers = new LinkedHashMap<>();

        handlers.put(AuthorizationRequest.class, new AuthorizationHandler());
        handlers.put(RegistrationRequest.class, new RegistrationHandler());
        handlers.put(ClientCommandsRequest.class, new ClientCommandsHandler());
        handlers.put(CommandExecutionRequest.class, new ClientCommandHandler());
        handlers.put(SingleArgumentCommandExecutionRequest.class, new ArgumentCommandHandler<>());
    }

    /**
     * Finds a matching request with a request from {@link ServerContext} and manages it using the
     * {@link RequestHandler#handleRequest(ServerContext)} method.
     *
     * @param context the specified server settings
     */
    public void manageRequest(ServerContext context) {
        try {
            Optional.ofNullable(handlers.get(context.getRequest().getClass())).orElseThrow(() ->
                    new IllegalManagerArgumentException("RequestHandlerManager contains illegal argument")).handleRequest(context);
        } catch (IllegalManagerArgumentException e) {
            logger.error("Failed to manage request", e);
            ErrorResponse errResponse = new ErrorResponse("Failed to manage request");
            new ResponseSender().sendResponse(context.getConnectionModule(), context.getRequestOrigin(), errResponse);
        }
    }
}