package clientModules.response.receivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.CommandExecutionRequestSender;
import clientModules.response.handlers.ExecutionResultHandler;
import commands.CommandDescription;
import commandsModule.handler.CommandHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.CommandExecutionRequest;
import response.responses.CommandExecutionResponse;

import java.io.IOException;
import java.io.StreamCorruptedException;

/**
 * A class that represents the command execution result receiver.
 */

public class ExecutionResultReceiver implements CommandReceiver {

    /**
     * A method that receives the simplified command, sends request to a server, gets response and
     * calls the {@link ExecutionResultHandler#handleResponse(CommandExecutionResponse)} method.
     *
     * @param command simplified command
     * @param args simplified command arguments
     * @param dataTransferConnectionModule client core
     */

    @Override
    public void receiveCommand(CommandDescription command, String[] args, DataTransferConnectionModule dataTransferConnectionModule) {
        CommandExecutionRequest commandRequest = new CommandExecutionRequest(command, args);
        CommandExecutionResponse executionResponse;
        try {
            executionResponse = new CommandExecutionRequestSender().sendRequest(dataTransferConnectionModule, commandRequest);

            new ExecutionResultHandler().handleResponse(executionResponse);

            CommandHandler.getMissedCommands().remove(command, args);
        } catch (StreamCorruptedException | ServerUnavailableException | ResponseTimeoutException e) {
            CommandHandler.getMissedCommands().put(command, args);
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        } catch (NullPointerException e) {
            System.out.println("Empty response received");
        }
    }

}
