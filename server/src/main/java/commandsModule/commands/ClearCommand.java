package commandsModule.commands;

import commands.CommandType;
import databaseModule.MemoryBackedDBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A class that implements the "clear" command.
 */
@Command
public class ClearCommand implements BaseCommand, CallerIdCommand {
    private static final Logger logger = LogManager.getLogger("logger.ClearCommand");
    private StringBuilder response;
    private int callerId;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "clear";
    }

    /**
     * A method that returns the response of the command.
     */
    @Override
    public String getResponse() {
        return this.response.toString();
    }

    /**
     * ClearCommand is a {@link CommandType#NO_ARGUMENT} command.
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
        return "Clears the collection";
    }

    @Override
    public void setCallerId(int callerId) {
        this.callerId = callerId;
    }

    /**
     * When called, clears the collection in the database.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        this.response = new StringBuilder();

        if (callerId == 0) {
            this.response.append("Execution failed. Server could not identify you");
            logger.debug("Unidentified user called ClearCommand");
            return;
        }

        boolean tryAgainLater = false;
        int result = MemoryBackedDBManager.getInstance().clearElementsInDBAndMemory(callerId);
        if (result == 1) {
            this.response.append("All elements were removed");
        } else if (result == 2) {
            this.response.append("No created elements to remove yet");
        } else if (result == -1) {
            this.response.append("Something went wrong. Some of the elements were not removed");
        } else {
            this.response.append("Something went wrong. No elements removed");
            tryAgainLater = true;
        }
        logger.info(this.response.toString());

        if (tryAgainLater) {
            this.response.append(". Please, try again later");
        }
    }
}