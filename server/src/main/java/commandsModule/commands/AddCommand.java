package commandsModule.commands;

import commands.CommandType;
import databaseModule.MemoryBackedDBManager;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import userModules.sessionService.AuthenticatedUserRegistry;
import userModules.users.AuthenticatedUser;

import java.io.IOException;

/**
 * A class that implements the "add" command.
 */
@Command
public class AddCommand implements ObjectArgumentCommand<Person>, CallerIdCommand {
    private static final Logger logger = LogManager.getLogger("logger.AddCommand");
    private StringBuilder response;
    private Person argument;
    private int callerId;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "add";
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
     * AddCommand is a {@link CommandType#SINGLE_OBJECT_ARGUMENT} command.
     *
     * @return the type of the command
     */
    @Override
    public CommandType getType() {
        return CommandType.SINGLE_OBJECT_ARGUMENT;
    }

    /**
     * Method that returns the description of the command.
     */
    @Override
    public String describe() {
        return "Adds element to collection";
    }

    @Override
    public void setCallerId(int callerId) {
        this.callerId = callerId;
    }

    /**
     * When called, adds received {@link Person} object to the collection.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        this.response = new StringBuilder();

        if (callerId == 0) {
            this.response.append("Execution failed. Server could not identify you");
            logger.debug("Unidentified user called AddCommand");
            return;
        }

        if (argument == null) {
            this.response.append("Execution failed. Server found null argument");
            logger.debug("Received null argument");
            return;
        }

        boolean tryAgainLater = false;
        if (MemoryBackedDBManager.getInstance().addElementToDBAndMemory(argument, callerId)) {
            this.response.append("Element was added");
        } else {
            this.response.append("Something went wrong. Element was not added");
            tryAgainLater = true;
        }
        logger.info(this.response.toString());

        if (tryAgainLater) {
            this.response.append(". Please, try again later");
        }
    }
}