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
import outputService.ColoredPrintStream;
import outputService.MessageType;
import outputService.OutputSource;
import requests.CommandExecutionRequest;
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
    private final RequestAble requestSender;
    private final ResponseVisitor responseVisitor;
    private final DataTransferConnectionModule dataTransferConnectionModule;

    public ScriptCommandReceiver(DataTransferConnectionModule dataTransferConnectionModule) {
        this.requestSender = new RequestSender(dataTransferConnectionModule);
        this.responseVisitor = new ResponseHandlerVisitor();
        this.dataTransferConnectionModule = dataTransferConnectionModule;
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

        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());

        try (InputStream inputStream = new FileInputStream(script)) {
            try {
                CommandExecutionRequest commandRequest = new CommandExecutionRequest(User.getInstance().getToken(), scriptCommand, args);

                Response response = requestSender.sendRequest(commandRequest);

                if (response.getClass().isAssignableFrom(CommandExecutionResponse.class)) {
                    response.accept(responseVisitor);
                    CommandHandler.getMissedCommandsMap().remove(scriptCommand, args);
                } else {
                    response.accept(responseVisitor);
                    CommandHandler.getMissedCommandsMap().put(scriptCommand, args);
                    return;
                }
            } catch (StreamCorruptedException | ServerUnavailableException | ResponseTimeoutException | NullPointerException e) {
                CommandHandler.getMissedCommandsMap().put(scriptCommand, args);
                return;
            }

            CommandManager commandManager = new CommandManager(dataTransferConnectionModule);

            String contents = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            String[] lines = contents.split("\n");
            String[] tokens;
            for (String line : lines) {
                tokens = line.split(" ");

                if (tokens[0].equals("execute_script")) {
                    historyOfDangerScript.add(args[0]);
                }

                CommandDescription command = CommandHandler.getCommandFromName(tokens[0].toLowerCase());
                if (command != null) {
                    commandManager.manageCommand(command, tokens);
                } else {
                    cps.println("Command \"" + tokens[0] + "\" Was Not Recognized as an" +
                            " Internal or External Command\nContinuing executing script...");
                }
            }
        } catch (IOException e) {
            cps.println(cps.formatMessage(MessageType.ERROR, "Unable to continue executing script. Returning to the console input"));
        }

        historyOfDangerScript.clear();
    }

    private boolean checkDanger(String[] args) {
        if (historyOfDangerScript.contains(args[0])) {
            ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());
            cps.println(cps.formatMessage(MessageType.WARNING, "Detected dangerous command: the script will loop if the command is executed"));
            cps.println("Continuing executing script from the next command...");
            historyOfDangerScript.clear();
            return false;
        }

        return true;
    }

    private boolean checkArgs(String[] args) {
        if (args.length < 2) {
            ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());
            cps.println("Not enough arguments. Returning to the console input");
            return false;
        }

        return true;
    }

    private boolean checkFile(File script) {
        if (!script.exists()) {
            ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());
            cps.println("File not found. Returning to the console input");
            return false;
        }

        return true;
    }
}