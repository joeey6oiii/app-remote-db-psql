package commandsModule.commands;

import commands.CommandType;
import databaseModule.handler.PersonCollectionHandler;
import model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that implements the "print_field_descending_birthday" command.
 */
@Command
public class PrintFieldDescendingBirthdayCommand implements BaseCommand {
    private static final Logger logger = LogManager.getLogger("logger.PrintFieldDescendingBirthdayCommand");
    private String response;

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "print_field_descending_birthday";
    }

    /**
     * A method that returns the response of the command.
     */
    @Override
    public String getResponse() {
        return this.response;
    }

    /**
     * PrintFieldDescendingBirthdayCommand is a {@link CommandType#NO_ARGUMENT} command.
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
        return "Outputs the \"birthday\" values of all elements in the database in descending order";
    }

    /**
     * When called, creates an <code>ArrayList</code> and adds the values of the birthday field of all the {@link Person}
     * objects in the collection to it. Then, sorts, reverses and sets the resulting list to the response field.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        PersonCollectionHandler personCollectionHandler = PersonCollectionHandler.getInstance();
        if (personCollectionHandler.getCollection().isEmpty()) {
            this.response = "Collection is empty, can not execute print_field_descending_birthday";
        } else {
            List<Date> list = personCollectionHandler.getCollection()
                    .stream()
                    .map(Person::getBirthday)
                    .sorted(Collections.reverseOrder())
                    .toList();

            StringBuilder builder;
            builder = new StringBuilder(list.stream()
                    .map(Date::toString)
                    .collect(Collectors.joining("\n")));
            this.response = new String(builder);
        }
        logger.info("Executed PrintFieldDescendingBirthdayCommand");
    }
}