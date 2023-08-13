package commandsModule.commands;

import commands.CommandType;
import comparators.HeightComparator;
import databaseModule.MemoryBackedDBManager;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements the "remove_greater" command.
 */
@Command
public class RemoveGreaterCommand implements BaseCommand, ObjectArgumentCommand<Person>, CallerIdCommand {
    private static final Logger logger = LogManager.getLogger("logger.RemoveGreaterCommand");
    private String response;
    private Person argument;
    private int callerId;

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
     * RemoveGreaterCommand is a {@link CommandType#SINGLE_OBJECT_ARGUMENT} command.
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
        return "Removes from the collection all elements greater than the specified";
    }

    @Override
    public void setCallerId(int callerId) {
        this.callerId = callerId;
    }

    /**
     * When called, removes all {@link Person} elements from the collection whose height field is greater than the
     * height field of the received created object.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        List<Person> ownerElements;
        MemoryBackedDBManager dbManager = MemoryBackedDBManager.getInstance();
        try {
            ownerElements = dbManager.getOwnerElements(callerId, ArrayList::new);
        } catch (SQLException e) {
            response = "Unable to load your elements. Please, try again later";
            logger.error("Error loading user database elements", e);
            return;
        }

        logger.info("Executed RemoveGreaterCommand");

        if (ownerElements.isEmpty()) {
            response = "Nothing to remove. You have not created any elements yet";
            return;
        }

        int counter = 0;
        HeightComparator comparator = new HeightComparator();

        for (Person currentPerson : ownerElements) {
            if (comparator.compare(currentPerson, argument) > 0 &&
                    dbManager.removeElementFromDBAndMemory(currentPerson.getId(), callerId) == 1) {
                counter++;
            }
        }

        response = "Removed " + counter + " elements whose height parameter is greater than " + argument.getHeight();
    }
}