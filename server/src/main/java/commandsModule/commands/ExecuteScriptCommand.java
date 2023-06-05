package commandsModule.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * A class that implements the "execute_script" command.
 */

public class ExecuteScriptCommand implements BaseCommand {
    private static final Logger logger = LogManager.getLogger("logger.ExecuteScriptCommand");

    /**
     * A method that returns the name of the command.
     */

    @Override
    public String getName() {
        return "execute_script";
    }

    /**
     * A method that returns the response of the command.
     */

    @Override
    public String getResponse() {
        return "Server is waiting to receive commands...";
    }

    /**
     * A method that returns the description of the command.
     */

    @Override
    public String describe() {
        return "Reads and executes a script from the specified file";
    }

    /**
     * When called, waits to receive commands from the client.
     *
     * @throws IOException when failed during I/O operations
     */

    @Override
    public void execute() throws IOException {
        logger.info("Executed ExecuteScriptCommand. Waiting to receive commands from the Client...");
    }

}
