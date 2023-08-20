package commandsModule.commands;

import commands.CommandType;
import databaseModule.MemoryBackedDBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Class that implements the "remove_by_id" command.
 */
@Command
public class RemoveByIdCommand implements ParameterizedCommand, CallerIdCommand {
    private static final Logger logger = LogManager.getLogger("logger.RemoveByIdCommand");
    private StringBuilder response;
    private String[] args;
    private int callerId;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "remove_by_id";
    }

    /**
     * A method that returns the response of the command.
     */
    @Override
    public String getResponse() {
        return this.response.toString();
    }

    /**
     * A method that returns arguments of the command.
     */
    @Override
    public String[] getArguments() {
        return this.args;
    }

    /**
     * A method that sets arguments to the command.
     *
     * @param args arguments of the command
     */
    @Override
    public void setArguments(String[] args) {
        this.args = args;
    }

    /**
     * RemoveByIdCommand is a {@link CommandType#NO_ARGUMENT} command.
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
        return "Removes an element from the database by id";
    }

    @Override
    public void setCallerId(int callerId) {
        this.callerId = callerId;
    }

    /**
     * When called, removes an element from the collection in the database by the specified id.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        this.response = new StringBuilder();

        if (callerId == 0) {
            this.response.append("Execution failed. Server could not identify you");
            logger.error("Unidentified user called RemoveByIdCommand");
            return;
        }

        if (args.length < 2) {
            this.response.append("Execution failed. Wrong number of arguments");
            logger.error("Wrong number of arguments for RemoveByIdCommand");
            return;
        }

        int elementId = Integer.parseInt(args[1]);
        if (elementId <= 0) {
            this.response.append("Execution failed. Wrong element id");
            logger.error("Wrong element id for RemoveByIdCommand");
            return;
        }

        boolean tryAgainLater = false;
        int result = MemoryBackedDBManager.getInstance().removeElementFromDBAndMemory(elementId, callerId);
        if (result == 1) {
            this.response.append("Removed element with id ").append(elementId);
        } else if (result == 0) {
            this.response.append("Something went wrong. Element with id ").append(elementId).append(" was not removed");
            tryAgainLater = true;
        } else {
            this.response.append("No access to element with id ").append(elementId).append(" or the element does not exist");
        }
        logger.info(this.response.toString());

        if (tryAgainLater) {
            this.response.append(". Please, try again later");
        }
    }
}