package commandsModule.commands;

import commands.CommandType;
import databaseModule.PersonCollectionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Class that implements the "remove_by_id" command.
 */
@Command
public class RemoveByIdCommand implements ParameterizedCommand {
    private static final Logger logger = LogManager.getLogger("logger.RemoveByIdCommand");
    private String response;
    private String[] args;

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

    /**
     * When called, removes an element from the collection in the database by the specified id.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        PersonCollectionHandler personCollectionHandler = PersonCollectionHandler.getInstance();
        int id = Integer.parseInt(args[1]);
        if (personCollectionHandler.getCollection().isEmpty()) {
            this.response = "Collection is empty, there is nothing to remove";
        } else {
            if (personCollectionHandler.removeElement(id)) {
                this.response = "Removed element with id " + id;
            } else {
                this.response = "No element matches id " + id;
            }
        }
        logger.info("Executed RemoveByIdCommand");
    }
}