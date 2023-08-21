package commandsModule.commands;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestAble;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ClientCommandsHandler;
import clientModules.response.visitor.ResponseHandlerVisitor;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.ClientCommandsRequest;
import response.responses.ClientCommandsResponse;
import response.responses.Response;
import response.visitor.ResponseVisitor;

import java.io.IOException;

/**
 * A class that represents the commands' receiver.
 */
public class CommandsReceiver {
    private final RequestAble requestSender;
    private final ResponseVisitor responseVisitor;

    public CommandsReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.requestSender = new RequestSender(dataTransferConnectionModule);
        this.responseVisitor = new ResponseHandlerVisitor();
    }

    /**
     * A method that gets simplified commands response and calls the
     * {@link ClientCommandsHandler#handleResponse(ClientCommandsResponse)})} method.
     *
     * @throws ServerUnavailableException if the server was unavailable during sending and receiving operations
     * @throws IOException if failed during I/O operations
     * @throws ResponseTimeoutException if client could not get response from the server during the given time
     */
    public boolean initCommands() throws ServerUnavailableException, ResponseTimeoutException, IOException {
        ClientCommandsRequest commandsRequest = new ClientCommandsRequest();
        Response response = requestSender.sendRequest(commandsRequest);

        if (response.getClass().isAssignableFrom(ClientCommandsResponse.class)) {
            return response.accept(responseVisitor);
        } else {
            response.accept(responseVisitor);
        }

        return false;
    }
}