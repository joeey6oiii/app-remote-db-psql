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
    private String response;
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
        return this.response;
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
        int result = MemoryBackedDBManager.getInstance().clearElementsInDBAndMemory(callerId);
        if (result == 1) {
            response = "All your elements were removed";
        } else if (result == 2) {
            response = "You have not created any elements yet";
        } else if (result == -1) {
            response = "Something went wrong. Some of your elements were not removed";
        } else {
            response = "Something went wrong. No elements removed. Please, try again later";
        }
        logger.info("Executed ClearCommand");
    }
}