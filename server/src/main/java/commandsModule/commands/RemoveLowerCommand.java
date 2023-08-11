package commandsModule.commands;

import commands.CommandType;
import comparators.HeightComparator;
import databaseModule.handler.PersonCollectionHandler;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A class that implements the "remove_lower" command.
 */
@Command
public class RemoveLowerCommand implements BaseCommand, ObjectArgumentCommand<Person> {
    private static final Logger logger = LogManager.getLogger("logger.RemoveLowerCommand");
    private String response;
    private Person argument;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "remove_lower";
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
    public Person getObjArgument() {
        return this.argument;
    }

    /**
     * A method that sets the {@link Person} argument to the command.
     */
    @Override
    public void setObjArgument(Person argument) {
        this.argument = argument;
    }

    /**
     * RemoveLowerCommand is a {@link CommandType#SINGLE_OBJECT_ARGUMENT} command.
     *
     * @return the type of the command
     */
    @Override
    public CommandType getType() {
        return CommandType.SINGLE_OBJECT_ARGUMENT;
    }

    /**
     * A method that returns the description of the command.
     */
    @Override
    public String describe() {
        return "Removes from the collection all elements lower than the specified";
    }

    /**
     * When called, removes all {@link Person} elements from the collection whose height field is less than the
     * height field of the received created object.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        PersonCollectionHandler personCollectionHandler = PersonCollectionHandler.getInstance();
        if (personCollectionHandler.getCollection().isEmpty()) {
            this.response = "Collection is empty, there is nothing to remove";
        } else {
            personCollectionHandler.getCollection().removeIf(p -> new HeightComparator().compare(p, argument) < 0);
            this.response = "Removed elements whose height parameter is lower than " + argument.getHeight();
        }
        logger.info("Executed RemoveLowerCommand");
    }
}