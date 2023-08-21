package commandsModule.commands;

import commands.CommandType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import userModules.sessionService.AuthenticatedUserRegistry;
import userModules.users.AuthenticatedUser;
import userModules.users.utils.UserUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that implements the "history" command.
 */
@Command
public class HistoryCommand implements CallerIdCommand {
    private static final Logger logger = LogManager.getLogger("logger.HistoryCommand");
    private StringBuilder response;
    private int callerId;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "history";
    }

    /**
     * A method that returns the response of the command.
     */
    @Override
    public String getResponse() {
        return this.response.toString();
    }

    /**
     * HistoryCommand is a {@link CommandType#NO_ARGUMENT} command.
     *
     * @return the type of the command
     */
    @Override
    public CommandType getType() {
        return CommandType.NO_ARGUMENT;
    }

    /**
     * A method that returns the description of the command.
     */
    @Override
    public String describe() {
        return "Outputs last input 9 commands without arguments";
    }

    @Override
    public void setCallerId(int callerId) {
        this.callerId = callerId;
    }

    /**
     * When called, gets the history from the {@link userModules.users.AuthenticatedUser} and checks the size of the history list.
     * If size equals zero, outputs a <code>String</code>. Otherwise, creates an <code>ArrayList</code> and populates it with the key
     * values from the command map using the map values obtained from the command history, and then checks the size again:
     * if size is between zero and nine, outputs the whole history <code>ArrayList</code>, otherwise removes the first values in
     * the history <code>ArrayList</code> until its size is nine, and then outputs the history <code>ArrayList</code>.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        this.response = new StringBuilder();

        if (callerId == 0) {
            this.response.append("Execution failed. Server could not identify you");
            logger.debug("Unidentified user called HistoryCommand");
            return;
        }

        AuthenticatedUser authenticatedUser = AuthenticatedUserRegistry.getInstance().getAuthenticatedUser(callerId);
        if (authenticatedUser == null) {
            this.response.append("You don't have permission to execute commands on the server");
            logger.debug("Unauthorized user tried to execute HistoryCommand");
            return;
        }

        List<BaseCommand> list = authenticatedUser.getCommandHistory();
        if (list.isEmpty()) {
            this.response.append("No command history yet");
            logger.info(this.response.toString());
        } else {
            List<String> history = list.stream()
                    .map(BaseCommand::getName)
                    .collect(Collectors.toList());
            int maxSizeValue = UserUtils.INSTANCE.getCommandHistoryMaxSizeValue();
            if (history.size() > maxSizeValue) {
                history = history.subList(history.size() - maxSizeValue, history.size());
            }
            this.response.append(history);
            logger.info("Compiled history up to 9 commands");
        }
    }
}