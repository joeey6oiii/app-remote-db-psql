package commandsModule.commands;

import commands.CommandType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import userModules.sessionService.AuthenticatedUserRegistry;

import java.io.IOException;

/**
 * Class that implements the "exit" command.
 */
@Command
public class ExitCommand implements BaseCommand, CallerIdCommand {
    private static final Logger logger = LogManager.getLogger("logger.ExitCommand");
    private int callerId;

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
        return "Disconnect allowed";
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

    @Override
    public void setCallerId(int callerId) {
        this.callerId = callerId;
    }

    /**
     * @throws IOException when failed during I/O operations
     */
    @Override
    public void execute() throws IOException {
        if (callerId == 0) {
            logger.error("Unidentified user called ExitCommand");
            return;
        }

        AuthenticatedUserRegistry userRegistry = AuthenticatedUserRegistry.getInstance();

        if (userRegistry.getAuthenticatedUser(callerId) == null) {
            logger.error("Unauthorized user tried to execute ExitCommand");
            return;
        }

        logger.info("User disconnects from the server");
        userRegistry.removeAuthenticatedUser(callerId);
    }
}