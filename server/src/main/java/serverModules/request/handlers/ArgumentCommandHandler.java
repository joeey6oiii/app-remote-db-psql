package serverModules.request.handlers;

import commandsModule.commands.SingleArgumentCommand;
import commandsModule.handler.CommandHandler;
import requests.CommandExecutionRequest;
import requests.SingleArgumentCommandExecutionRequest;
import serverModules.callerBack.CallerBack;
import serverModules.connection.ConnectionModule;
import serverModules.context.ServerContext;

/**
 * A class that works with the client single argument command execution request.
 *
 * @param <T> the specified argument
 */

public class ArgumentCommandHandler<T> implements RequestHandler {

    /**
     * A method that handles the client single argument command execution request and calls the
     * {@link CommandHandler#execute(ConnectionModule, CallerBack, CommandExecutionRequest)} method.
     *
     * @param context the specified server settings
     */

    @Override
    public void handleRequest(ServerContext context) {
        CommandHandler commandHandler = new CommandHandler();

        SingleArgumentCommandExecutionRequest<T> executionRequest = (SingleArgumentCommandExecutionRequest<T>) context.getRequest();

        SingleArgumentCommand<T> command = (SingleArgumentCommand<T>) commandHandler.getCommandByDescription(executionRequest.getDescriptionCommand());
        command.setSingleArgument(executionRequest.getArg());

        commandHandler.execute(context.getConnectionModule(), context.getCallerBack(), executionRequest);
    }
}