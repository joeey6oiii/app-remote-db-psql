package commandsModule.commands;

import commands.CommandType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Class that implements the "exit" command.
 */
@Command
public class ExitCommand implements BaseCommand {
    private static final Logger logger = LogManager.getLogger("logger.ExitCommand");

    /**
     * A method that returns the name of the command.
     */
    @Override
    public String getName() {
        return "exit";
    }

    /**
     * A method that returns the response of the command.
     */
    @Override
    public String getResponse() {
        return "Standard \"exit\" command response";
    }

    /**
     * ExitCommand is a {@link CommandType#EXIT_SECTION} command.
     *
     * @return the type of the command
     */
    @Override
    public CommandType getType() {
        return CommandType.EXIT_SECTION;
    }

    /**
     * A method that returns the description of the command.
     */
    @Override
    public String describe() {
        return "Closes the program without saving";
    }

    /**
     * When called, calls {@link SaveCommand#execute()} method to save the database data to the file.
     *
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        logger.info("Client disconnects");
        new SaveCommand().execute();
    }
}
