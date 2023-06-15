package clientModules.response.receivers;

import clientModules.Client;
import clientModules.authentication.AuthenticationManager;
import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ExecutionResultHandler;
import clientModules.response.handlers.ExitCommandHandler;
import commands.CommandDescription;
import commandsModule.handler.CommandHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.CommandExecutionRequest;
import response.responses.AuthorizationResponse;
import response.responses.CommandExecutionResponse;
import response.responses.Response;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.Scanner;

/**
 * A class that represents the exit command receiver.
 */

public class ExitCommandReceiver implements CommandReceiver {

    /**
     * A method that receives the simplified "exit" command, sends request to a server,
     * gets response and calls the {@link ExitCommandHandler#handleResponse(CommandExecutionResponse)})} method.
     *
     * @param command simplified command
     * @param args simplified command arguments
     * @param dataTransferConnectionModule client core
     */

    @Override
    public void receiveCommand(CommandDescription command, String[] args, DataTransferConnectionModule dataTransferConnectionModule) {
        System.out.print("Are you sure you want to exit? [Y/N]\n$ ");
        Scanner consoleInputReader = new Scanner(System.in);
        String consoleInput;

        while (!(consoleInput = consoleInputReader.nextLine()).equalsIgnoreCase("Y")) {
            if (consoleInput.equalsIgnoreCase("N")) {
                System.out.println("Returning to the console input");
                CommandHandler.getMissedCommands().remove(command, args);
                return;
            }
            System.out.print("$ ");
        }

        CommandExecutionRequest commandRequest = new CommandExecutionRequest(Client.getLogin(), Client.getPassword(), command, args);
        Response response;
        try {
            response = new RequestSender().sendRequest(dataTransferConnectionModule, commandRequest);

            if(response instanceof CommandExecutionResponse executionResponse) {
                new ExitCommandHandler().handleResponse(executionResponse);

                CommandHandler.getMissedCommands().remove(command, args);
            } else if(response instanceof AuthorizationResponse authorizationResponse && !authorizationResponse.isSuccess()) {
                new AuthenticationManager(dataTransferConnectionModule).authenticateFromInput();
                CommandHandler.getMissedCommands().put(command, args);
            } else {
                System.out.println("Received invalid response from server");
                CommandHandler.getMissedCommands().put(command, args);
            }

        } catch (StreamCorruptedException | ServerUnavailableException | ResponseTimeoutException e) {
            CommandHandler.getMissedCommands().put(command, args);
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        } catch (NullPointerException e) {
            System.out.println("Empty response received");
        }
    }

}
