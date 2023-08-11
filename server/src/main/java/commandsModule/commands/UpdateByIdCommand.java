package commandsModule.commands;

import commands.CommandType;
import databaseModule.handler.PersonCollectionHandler;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * A class that implements the "update_by_id" command.
 */
@Command
public class UpdateByIdCommand implements ParameterizedCommand, ObjectArgumentCommand<Person> {
    private static final Logger logger = LogManager.getLogger("logger.UpdateByIdCommand");
    private String response;
    private String[] args;
    private Person argument;

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
        return this.response;
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

    /**
     * When called, iterates through the collection to find the {@link Person} object with the specified id. If not found,
     * outputs <code>String</code>, otherwise replaces in the database current {@link Person} with the specified id
     * with the received created {@link Person} object.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        PersonCollectionHandler personCollectionHandler = PersonCollectionHandler.getInstance();
        int id = Integer.parseInt(args[1]);
        Optional<Person> optionalPerson = personCollectionHandler.getCollection()
                .stream().filter(p -> Objects.equals(p.getId(), id)).findFirst();
        if (optionalPerson.isPresent()) {
            Person existingPerson = optionalPerson.get();
            personCollectionHandler.getCollection().remove(existingPerson);
            argument.setId(id);
            personCollectionHandler.addElement(argument);
            this.response ="Updated element with id " + id;
        } else {
            this.response = "No element matches id " + id;
        }
        logger.info("Executed UpdateByIdCommand");
    }
}