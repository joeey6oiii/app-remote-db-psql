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
    private String response;
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
        return this.response;
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
        int elementId = Integer.parseInt(args[1]);

        int result = MemoryBackedDBManager.getInstance().removeElementFromDBAndMemory(elementId, callerId);
        if (result == 1) {
            response = "Removed element with id " + elementId;
        } else if (result == 0) {
            response = "Something went wrong. Element with id " + elementId + " was not removed. Please, try again later";
        } else {
            response = "You have no access to remove element with id " + elementId;
        }
        logger.info("Executed RemoveByIdCommand");
    }
}