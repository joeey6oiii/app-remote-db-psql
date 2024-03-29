package commandsModule.commands;

import commands.CommandType;
import databaseModule.handler.PersonCollectionHandler;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A class that implements the "average_of_height" command.
 */
@Command
public class AverageOfHeightCommand implements BaseCommand {
    private static final Logger logger = LogManager.getLogger("logger.AverageOfHeightCommand");
    private StringBuilder response;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "average_of_height";
    }

    /**
     * A method that returns the response of the command.
     */
    @Override
    public String getResponse() {
        return this.response.toString();
    }

    /**
     * AverageOfHeightCommand is a {@link CommandType#NO_ARGUMENT} command.
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
        return "Outputs the average value of the \"height\" field of all elements in the database";
    }

    /**
     * When called, sums the height field values of all {@link Person} objects and counts the amount of objects in the
     * collection, then outputs the average value by dividing sum by count.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        this.response = new StringBuilder();

        PersonCollectionHandler personCollectionHandler = PersonCollectionHandler.getInstance();
        if (personCollectionHandler.getCollection().isEmpty()) {
            this.response.append("Collection is empty, can not execute average_of_height");
        } else {
            double averageHeight = personCollectionHandler.getCollection()
                    .stream().mapToInt(Person::getHeight).average().orElse(0);
            this.response.append("The average \"height\" value is ").append(averageHeight);
        }
        logger.info("Counted average \"height\" value");
    }
}