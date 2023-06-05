package clientModules.request.sender;

import clientModules.connection.DataTransferConnectionModule;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.Request;
import requests.SingleArgumentCommandExecutionRequest;
import response.responses.CommandExecutionResponse;

import java.io.IOException;

/**
 * A class that represents the single argument command execution request sender.
 */

public class SingleArgumentCommandExecutionRequestSender implements RequestAble<CommandExecutionResponse, SingleArgumentCommandExecutionRequest<?>> {

    /**
     * A method that calls {@link RequestSender#sendRequest(DataTransferConnectionModule, Request)} method.
     *
     * @param module client core
     * @param request concrete request
     * @throws IOException if failed during I/O operations
     * @throws ServerUnavailableException if the server was unavailable during sending and receiving operations
     * @return argument command execution result response
     */

    @Override
    public CommandExecutionResponse sendRequest(DataTransferConnectionModule module, SingleArgumentCommandExecutionRequest<?> request) throws IOException, ServerUnavailableException, ResponseTimeoutException {
        return (CommandExecutionResponse) new RequestSender().sendRequest(module, request);
    }

}
