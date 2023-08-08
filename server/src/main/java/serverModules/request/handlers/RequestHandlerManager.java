package serverModules.request.handlers;

import exceptions.IllegalManagerArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.*;
import response.responses.ErrorResponse;
import serverModules.request.data.ClientRequestInfo;
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
        handlers.put(ObjectArgumentCommandExecutionRequest.class, new ObjArgCommandHandler<>());
    }

    /**
     * Finds a matching request with a request from {@link ClientRequestInfo} and manages it using the
     * {@link RequestHandler#handleRequest(ClientRequestInfo)} method.
     *
     * @param info information about the request
     */
    public void manageRequest(ClientRequestInfo info) {
        try {
            Optional.ofNullable(handlers.get(info.getRequest().getClass())).orElseThrow(() ->
                    new IllegalManagerArgumentException("RequestHandlerManager contains illegal argument")).handleRequest(info);
        } catch (IllegalManagerArgumentException e) {
            logger.error("Failed to manage request", e);
            ErrorResponse errResponse = new ErrorResponse("Failed to manage request. Please try again later");
            new ResponseSender().sendResponse(info.getConnectionModule(), info.getRequestOrigin(), errResponse);
        }
    }
}