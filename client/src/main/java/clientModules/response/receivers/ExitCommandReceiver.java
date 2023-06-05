package clientModules.response.receivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.CommandExecutionRequestSender;
import clientModules.response.handlers.ExitCommandHandler;
import commands.CommandDescription;
import commandsModule.handler.CommandHandler;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import requests.CommandExecutionRequest;
import response.responses.CommandExecutionResponse;

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

        CommandExecutionRequest commandRequest = new CommandExecutionRequest(command, args);
        CommandExecutionResponse executionResponse;
        try {
            executionResponse = new CommandExecutionRequestSender().sendRequest(dataTransferConnectionModule, commandRequest);

            new ExitCommandHandler().handleResponse(executionResponse);

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
