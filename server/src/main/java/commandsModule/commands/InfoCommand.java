package commandsModule.commands;

import database.Database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Class that implements the "info" command.
 */

public class InfoCommand implements BaseCommand {
    private static final Logger logger = LogManager.getLogger("logger.InfoCommand");
    private String response;

    /**
     * A method that returns the name of the command.
     */

    @Override
    public String getName() {
        return "info";
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
        return "Prints information about the collection to the standard" +
                " output stream (type, initialization date, number of elements, etc.)";
    }

    /**
     * When called, displays the information about the database (Type, Length, Initialization Time).
     *
     * @throws IOException when failed during I/O operations
     */

    @Override
    public void execute() throws IOException {
        Database database = Database.getInstance();
        StringBuilder builder = new StringBuilder();
        builder.append("Type: ").append(database.getCollection().getClass()).append("\nLength: ")
                .append(database.getCollection().size()).append("\nInitialization Time: ")
                .append(database.getInitializationTime());
        this.response = new String(builder);
        logger.info("Executed InfoCommand");
    }

}
