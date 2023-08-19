package commandsModule.commands;

import commands.CommandType;
import databaseModule.handler.PersonCollectionHandler;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A class that implements the "sum_of_height" command.
 */
@Command
public class SumOfHeightCommand implements BaseCommand {
    private static final Logger logger = LogManager.getLogger("logger.SumOfHeightCommand");
    private StringBuilder response;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "sum_of_height";
    }

    /**
     * A method that returns the response of the command.
     */
    @Override
    public String getResponse() {
        return this.response.toString();
    }

    /**
     * SumOfHeightCommand is a {@link CommandType#NO_ARGUMENT} command.
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
        return "Outputs the sum of the \"height\" values of all elements in the database";
    }

    /**
     * When called, sums the height field values of all {@link Person} objects in the collection, then sets the
     * resulting sum to the response field.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        this.response = new StringBuilder();

        PersonCollectionHandler personCollectionHandler = PersonCollectionHandler.getInstance();
        if (personCollectionHandler.getCollection().isEmpty()) {
            this.response.append("Collection is empty, can not execute sum_of_height");
            logger.info(this.response.toString());
        } else {
            int sum = personCollectionHandler.getCollection().stream().mapToInt(Person::getHeight).sum();
            this.response.append("Sum of \"height\" values is ").append(sum);
            logger.info("Counted sum of \"height\" values");
        }
    }
}