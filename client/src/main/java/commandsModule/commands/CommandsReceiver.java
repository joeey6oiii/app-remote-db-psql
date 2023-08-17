package commandsModule.commands;

import clientModules.authentication.User;
import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ClientCommandsHandler;
import clientModules.response.handlers.ServerErrorResultHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.ClientCommandsRequest;
import response.responses.ClientCommandsResponse;
import response.responses.ErrorResponse;
import response.responses.Response;

import java.io.IOException;

/**
 * A class that represents the commands' receiver.
 */
public class CommandsReceiver {
    private final DataTransferConnectionModule dataTransferConnectionModule;

    public CommandsReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;
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
        Response response = new RequestSender().sendRequest(dataTransferConnectionModule, commandsRequest);

        if (response instanceof ErrorResponse errResponse) {
            new ServerErrorResultHandler().handleResponse(errResponse);
        } else if (response instanceof ClientCommandsResponse commandsResponse){
            return new ClientCommandsHandler().handleResponse(commandsResponse);
        }

        return false;
    }
}