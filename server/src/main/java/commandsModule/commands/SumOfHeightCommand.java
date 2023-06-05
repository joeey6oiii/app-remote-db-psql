package commandsModule.commands;

import database.Database;
import defaultClasses.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A class that implements the "sum_of_height" command.
 */

public class SumOfHeightCommand implements BaseCommand {
    private static final Logger logger = LogManager.getLogger("logger.SumOfHeightCommand");
    private String response;

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
        return this.response;
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
        Database database = Database.getInstance();
        if (database.getCollection().isEmpty()) {
            this.response = "Collection is empty, can not execute sum_of_height";
        } else {
            int sum = database.getCollection().stream().mapToInt(Person::getHeight).sum();
            this.response = "Sum of \"height\" values is " + sum;
        }
        logger.info("Executed SumOfHeightCommand");
    }
}
