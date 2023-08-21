package serverModules.request.handlers;

import exceptions.IllegalManagerArgumentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import requests.*;
import response.responses.ErrorResponse;
import serverModules.connection.ConnectionModule;
import serverModules.request.data.ClientRequestInfo;
import serverModules.request.handlers.authenticationHandlers.AuthorizationHandler;
import serverModules.request.handlers.authenticationHandlers.RegistrationHandler;
import serverModules.response.sender.ChunkedResponseSender;
import serverModules.response.sender.ResponseSender;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class that manages the received requests.
 */
public class RequestHandlerManager {
    private static final Logger logger = LogManager.getLogger("logger.RequestHandlerManager");
    private static final ExecutorService requestManagingThreadPool = Executors.newFixedThreadPool(2);
    private final LinkedHashMap<Class<? extends Request>, RequestHandler> handlers;
    private final ResponseSender responseSender;

    public RequestHandlerManager(ConnectionModule connectionModule) {
        handlers = new LinkedHashMap<>();
        responseSender = new ChunkedResponseSender(connectionModule);

        handlers.put(AuthorizationRequest.class, new AuthorizationHandler(connectionModule));
        handlers.put(RegistrationRequest.class, new RegistrationHandler(connectionModule));
        handlers.put(ClientCommandsRequest.class, new ClientCommandsHandler(connectionModule));
        handlers.put(CommandExecutionRequest.class, new ClientCommandHandler(connectionModule));
        handlers.put(ObjectArgumentCommandExecutionRequest.class, new ObjArgCommandHandler<>(connectionModule));
    }

    /**
     * Finds a matching request with a request from {@link ClientRequestInfo} and manages it using the
     * {@link RequestHandler#handleRequest(ClientRequestInfo)} method.
     *
     * @param info information about the request
     */
    public void manageRequest(ClientRequestInfo info) {
        requestManagingThreadPool.submit(() -> {
            try {
                Optional.ofNullable(handlers.get(info.getRequest().getClass())).orElseThrow(() ->
                        new IllegalManagerArgumentException("RequestHandlerManager contains illegal argument")).handleRequest(info);
            } catch (IllegalManagerArgumentException e) {
                logger.error("Failed to manage request", e);
                ErrorResponse errResponse = new ErrorResponse("Failed to manage request. Please try again later");
                responseSender.sendResponse(info.getRequesterUser(), errResponse);
            }
        });
    }
}