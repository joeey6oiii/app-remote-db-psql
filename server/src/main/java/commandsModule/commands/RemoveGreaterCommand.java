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
    private StringBuilder response;
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
        return this.response.toString();
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
        this.response = new StringBuilder();

        if (callerId == 0) {
            this.response.append("Execution failed. Server could not identify you");
            logger.error("Unidentified user called RemoveGreaterCommand");
            return;
        }

        if (argument == null) {
            this.response.append("Execution failed. Server found null argument");
            logger.error("Received null argument");
            return;
        }

        List<Person> ownerElements;
        MemoryBackedDBManager dbManager = MemoryBackedDBManager.getInstance();
        try {
            ownerElements = dbManager.getOwnerElements(callerId, ArrayList::new);
            logger.info("Loaded user database elements");
        } catch (SQLException e) {
            this.response.append("Failed to load your elements. Please, try again later");
            logger.error("Error loading user database elements", e);
            return;
        }

        if (ownerElements.isEmpty()) {
            this.response.append("No created elements to remove yet");
            logger.info(this.response.toString());
            return;
        }

        int counter = 0;
        HeightComparator comparator = new HeightComparator();

        logger.info("Started comparing elements");
        for (Person currentPerson : ownerElements) {
            if (comparator.compare(currentPerson, argument) > 0 &&
                    dbManager.removeElementFromDBAndMemory(currentPerson.getId(), callerId) == 1) {
                counter++;
            }
        }

        this.response.append("Removed ").append(counter).append(" elements whose height parameter is greater than ").append(argument.getHeight());
        logger.info("Removed " + counter + " elements");
    }
}