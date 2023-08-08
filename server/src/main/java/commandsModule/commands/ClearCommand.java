package commandsModule.commands;

import commands.CommandType;
import databaseModule.PersonCollectionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A class that implements the "clear" command.
 */
@Command
public class ClearCommand implements BaseCommand {
    private static final Logger logger = LogManager.getLogger("logger.ClearCommand");
    private String response;

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

    /**
     * When called, clears the collection in the database.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        PersonCollectionHandler personCollectionHandler = PersonCollectionHandler.getInstance();
        if (personCollectionHandler.getCollection().isEmpty()) {
            this.response = "Collection is empty, there is nothing to clear";
        } else {
            personCollectionHandler.clearCollection();
            this.response = "Cleared the collection";
        }
        logger.info("Executed ClearCommand");
    }
}