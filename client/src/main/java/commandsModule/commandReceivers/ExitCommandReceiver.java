package commandsModule.commandReceivers;

import clientModules.authentication.AuthenticationManager;
import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ExitCommandHandler;
import clientModules.response.handlers.ServerErrorResultHandler;
import clientModules.authentication.User;
import commands.CommandDescription;
import commandsModule.commandsManagement.CommandHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.CommandExecutionRequest;
import response.responses.AuthorizationResponse;
import response.responses.CommandExecutionResponse;
import response.responses.ErrorResponse;
import response.responses.Response;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.Scanner;

/**
 * A class that represents the exit command receiver.
 */
public class ExitCommandReceiver implements CommandReceiver {
    private final DataTransferConnectionModule dataTransferConnectionModule;

    public ExitCommandReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.dataTransferConnectionModule = dataTransferConnectionModule;
    }

    /**
     * A method that receives the simplified "exit" command, sends request to a server,
     * gets response and calls the {@link ExitCommandHandler#handleResponse(CommandExecutionResponse)})} method.
     *
     * @param command simplified command
     * @param args simplified command arguments
     */
    @Override
    public void receiveCommand(CommandDescription command, String[] args) {
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

        CommandExecutionRequest commandRequest = new CommandExecutionRequest(User.getInstance().getToken(), command, args);
        Response response;
        try {
            response = new RequestSender(dataTransferConnectionModule).sendRequest(commandRequest);
            boolean isSuccess = false;

            if (response instanceof ErrorResponse errResponse) {
                new ServerErrorResultHandler().handleResponse(errResponse);
            } else if (response instanceof CommandExecutionResponse executionResponse) {
                isSuccess = new ExitCommandHandler().handleResponse(executionResponse);
            } else if (response instanceof AuthorizationResponse authorizationResponse && !authorizationResponse.isSuccess()) {
                new AuthenticationManager(dataTransferConnectionModule).authenticateFromInput();
            } else {
                System.out.println("Received invalid response from server");
            }

            if (isSuccess) {
                CommandHandler.getMissedCommands().remove(command, args);
            } else {
                CommandHandler.getMissedCommands().put(command, args);
            }

        } catch (StreamCorruptedException | ServerUnavailableException | ResponseTimeoutException e) {
            CommandHandler.getMissedCommands().put(command, args);
        } catch (IOException e) {
            System.out.println("Something went wrong during I/O operations");
        } catch (NullPointerException e) {
            System.out.println("Empty response received");
            CommandHandler.getMissedCommands().put(command, args);
        }
    }
}