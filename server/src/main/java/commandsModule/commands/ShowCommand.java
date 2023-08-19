package commandsModule.commands;

import commands.CommandType;
import databaseModule.handler.PersonCollectionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * A class that implements the "show" command.
 */
@Command
public class ShowCommand implements BaseCommand {
    private static final Logger logger = LogManager.getLogger("logger.ShowCommand");
    private StringBuilder response;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "show";
    }

    /**
     * A method that returns the response of the command.
     */
    @Override
    public String getResponse() {
        return this.response.toString();
    }

    /**
     * ShowCommand is a {@link CommandType#NO_ARGUMENT} command.
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
        return "Prints to standard output all elements of the collection in string representation";
    }

    /**
     * When called, shows user all elements from the collection.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        PersonCollectionHandler personCollectionHandler = PersonCollectionHandler.getInstance();
        if (personCollectionHandler.getCollection().isEmpty()) {
            this.response = new StringBuilder();
            this.response.append("Collection is empty, there is nothing to show");
            logger.info(this.response.toString());
        } else {
            this.response = new StringBuilder(personCollectionHandler.getCollection()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n")));
            logger.info("Compiled elements from the collection");
        }
    }
}