package commandsModule.commands;

import comparators.HeightComparator;
import database.Database;
import defaultClasses.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A class that implements the "remove_greater" command.
 */

public class RemoveGreaterCommand implements BaseCommand, SingleArgumentCommand<Person> {
    private static final Logger logger = LogManager.getLogger("logger.RemoveGreaterCommand");
    private String response;
    private Person argument;

    /**
     * A method that returns the name of the command.
     */

    @Override
    public String getName() {
        return "remove_greater";
    }

    /**
     * A method that returns the response of the command.
     */

    @Override
    public String getResponse() {
        return this.response;
    }

    /**
     * A method that returns the {@link Person} argument of the command.
     */

    @Override
    public Person getSingleArgument() {
        return this.argument;
    }

    /**
     * A method that sets the {@link Person} argument to the command.
     */

    @Override
    public void setSingleArgument(Person argument) {
        this.argument = argument;
    }

    /**
     * A method that returns the description of the command.
     */

    @Override
    public String describe() {
        return "Removes from the collection all elements greater than the specified";
    }

    /**
     * When called, removes all {@link Person} elements from the collection whose height field is greater than the
     * height field of the received created object.
     *
     * @throws IOException when failed during I/O operations
     */

    @Override
    public void execute() throws IOException {
        Database database = Database.getInstance();
        if (database.getCollection().isEmpty()) {
            this.response = "Collection is empty, there is nothing to remove";
        } else {
            database.getCollection().removeIf(p -> new HeightComparator().compare(p, argument) > 0);
            this.response = "Removed elements whose height parameter is greater than " + argument.getHeight();
        }
        logger.info("Executed RemoveGreaterCommand");
    }

}