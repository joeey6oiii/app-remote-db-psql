package commandsModule.commandReceivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestAble;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ExecutionResultHandler;
import clientModules.authentication.User;
import clientModules.response.visitor.ResponseHandlerVisitor;
import commands.CommandDescription;
import commandsModule.commandsManagement.CommandHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.CommandExecutionRequest;
import requests.Request;
import response.responses.CommandExecutionResponse;
import response.responses.Response;
import response.visitor.ResponseVisitor;

import java.io.IOException;
import java.io.StreamCorruptedException;

/**
 * A class that represents the command execution result receiver.
 */
public class ExecutionResultReceiver implements CommandReceiver {
    private final RequestAble<Response, Request> requestSender;
    private final ResponseVisitor responseVisitor;

    public ExecutionResultReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.requestSender = new RequestSender(dataTransferConnectionModule);
        this.responseVisitor = new ResponseHandlerVisitor();
    }

    /**
     * A method that receives the simplified command, sends request to a server, gets response and
     * calls the {@link ExecutionResultHandler#handleResponse(CommandExecutionResponse)} method.
     *
     * @param command simplified command
     * @param args simplified command arguments
     */
    @Override
    public void receiveCommand(CommandDescription command, String[] args) {
        CommandExecutionRequest commandRequest = new CommandExecutionRequest(User.getInstance().getToken(), command, args);
        Response response;
        try {
            response = requestSender.sendRequest(commandRequest);

            if (response.getClass().isAssignableFrom(CommandExecutionResponse.class)) {
                response.accept(responseVisitor);
                CommandHandler.getMissedCommands().remove(command, args);
            } else {
                response.accept(responseVisitor);
                CommandHandler.getMissedCommands().put(command, args);
            }
        } catch (StreamCorruptedException | ServerUnavailableException
                 | ResponseTimeoutException | NullPointerException e) {
            CommandHandler.getMissedCommands().put(command, args);
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        }
    }
}