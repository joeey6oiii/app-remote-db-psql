package commandsModule.commands;

import commands.CommandType;
import databaseModule.MemoryBackedDBManager;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A class that implements the "update_by_id" command.
 */
@Command
public class UpdateByIdCommand implements ParameterizedCommand, ObjectArgumentCommand<Person>, CallerIdCommand {
    private static final Logger logger = LogManager.getLogger("logger.UpdateByIdCommand");
    private StringBuilder response;
    private String[] args;
    private Person argument;
    private int callerId;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "update_by_id";
    }

    /**
     * A method that returns the response of the command.
     */
    @Override
    public String getResponse() {
        return this.response.toString();
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
     * UpdateByIdCommand is a {@link CommandType#SINGLE_OBJECT_ARGUMENT} command.
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
        return "Updates element in database by specified ID";
    }

    @Override
    public void setCallerId(int callerId) {
        this.callerId = callerId;
    }

    /**
     * When called, iterates through the collection to find the {@link Person} object with the specified id. If not found,
     * outputs <code>String</code>, otherwise replaces in the database current {@link Person} with the specified id
     * with the received created {@link Person} object.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        this.response = new StringBuilder();

        if (callerId == 0) {
            this.response.append("Execution failed. Server could not identify you");
            logger.debug("Unidentified user called UpdateByIdCommand");
            return;
        }

        if (argument == null) {
            this.response.append("Execution failed. Server found null argument");
            logger.debug("Received null argument");
            return;
        }

        if (args.length < 2) {
            this.response.append("Invalid number of arguments");
            logger.info("Invalid number of arguments for UpdateByIdCommand");
            return;
        }

        int elementId = Integer.parseInt(args[1]);
        MemoryBackedDBManager dbManager = MemoryBackedDBManager.getInstance();

        boolean tryAgainLater = false;
        int result = dbManager.updateElementInDBAndMemory(argument, elementId, callerId);
        if (result == 1) {
            this.response.append("Updated element with id ").append(elementId);
        } else if (result == 0) {
            this.response.append("Something went wrong. Element with id ").append(elementId).append(" was not updated");
            tryAgainLater = true;
        } else {
            this.response.append("No access to element with id ").append(elementId).append(" or the element does not exist");
        }
        logger.info(this.response.toString());

        if (tryAgainLater) {
            this.response.append(". Please, try again later");
        }
    }
}