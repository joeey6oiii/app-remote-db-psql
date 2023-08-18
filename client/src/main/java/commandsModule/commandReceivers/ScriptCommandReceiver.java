package commandsModule.commandReceivers;

import clientModules.connection.DataTransferConnectionModule;
import clientModules.request.sender.RequestAble;
import clientModules.request.sender.RequestSender;
import clientModules.response.handlers.ExecutionResultHandler;
import clientModules.authentication.User;
import clientModules.response.visitor.ResponseHandlerVisitor;
import commands.CommandDescription;
import commandsModule.commandsManagement.CommandHandler;
import commandsModule.commandsManagement.CommandManager;
import exceptions.ResponseTimeoutException;
import exceptions.ServerUnavailableException;
import org.apache.commons.io.IOUtils;
import requests.CommandExecutionRequest;
import requests.Request;
import response.responses.CommandExecutionResponse;
import response.responses.Response;
import response.visitor.ResponseVisitor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * A class that represents the script command receiver.
 */
public class ScriptCommandReceiver implements CommandReceiver {
    private static final LinkedList<String> historyOfDangerScript = new LinkedList<>();
    private final RequestAble<Response, Request> requestSender;
    private final ResponseVisitor responseVisitor;
    private final CommandManager commandManager;

    public ScriptCommandReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.requestSender = new RequestSender(dataTransferConnectionModule);
        this.responseVisitor = new ResponseHandlerVisitor();
        this.commandManager = new CommandManager(dataTransferConnectionModule);
    }

    /**
     * A method that receives the simplified "execute_script" command, parses file, sends request to a server,
     * gets response and calls the {@link ExecutionResultHandler#handleResponse(CommandExecutionResponse)} method.
     * After, iterates through <code>String[]</code> of commands from the parsed file
     * and calls the {@link CommandManager#manageCommand(CommandDescription, String[])} method.
     * Stores "history of danger scripts" collection to avoid looping
     *
     * @param scriptCommand simplified command
     * @param args simplified command arguments
     */
    @Override
    public void receiveCommand(CommandDescription scriptCommand, String[] args) {
        if (!this.checkDanger(args) || !this.checkArgs(args)) {
            return;
        }

        File script = new File(args[1]);
        if (!this.checkFile(script)) {
            return;
        }

        try (InputStream inputStream = new FileInputStream(script)) {
            try {
                CommandExecutionRequest commandRequest = new CommandExecutionRequest(User.getInstance().getToken(), scriptCommand, args);

                Response response = requestSender.sendRequest(commandRequest);

                if (response.getClass().isAssignableFrom(CommandExecutionResponse.class)) {
                    response.accept(responseVisitor);
                    CommandHandler.getMissedCommands().remove(scriptCommand, args);
                } else {
                    response.accept(responseVisitor);
                    CommandHandler.getMissedCommands().put(scriptCommand, args);
                    return;
                }
            } catch (StreamCorruptedException | ServerUnavailableException | ResponseTimeoutException | NullPointerException e) {
                CommandHandler.getMissedCommands().put(scriptCommand, args);
                return;
            }

            String contents = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            String[] lines = contents.split("\n");
            String[] tokens;
            for (String line : lines) {
                tokens = line.split(" ");

                if (tokens[0].equals("execute_script")) {
                    historyOfDangerScript.add(args[0]);
                }

                CommandDescription command = CommandHandler.getCommandByName(tokens[0].toLowerCase());
                if (command != null) {
                    commandManager.manageCommand(command, tokens);
                } else {
                    System.out.println("Command \"" + tokens[0] + "\" Was Not Recognized as an" +
                            " Internal or External Command\nContinuing executing script...");
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to continue executing script. Returning to the console input");
        }

        historyOfDangerScript.clear();
    }

    private boolean checkDanger(String[] args) {
        if (historyOfDangerScript.contains(args[0])) {
            System.out.println("Detected dangerous command: the script will loop if the command is executed\n" +
                    "Continuing executing script from the next command...");
            historyOfDangerScript.clear();
            return false;
        }

        return true;
    }

    private boolean checkArgs(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments. Returning to the console input");
            return false;
        }

        return true;
    }

    private boolean checkFile(File script) {
        if (!script.exists()) {
            System.out.println("File not found. Returning to the console input");
            return false;
        }

        return true;
    }
}