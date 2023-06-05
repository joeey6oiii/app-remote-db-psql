package commandsModule.commands;

import java.io.IOException;

/**
 * An interface for all command-implementers without arguments.
 */

public interface BaseCommand {

    /**
     * A method that returns the name of the command.
     */

    String getName();

    /**
     * A method that returns the response of the command.
     */

    String getResponse();

    /**
     * A method that returns the description of the command.
     */

    String describe();

    /**
     * Command call method.
     *
     * @throws IOException when failed during I/O operations
     */

    void execute() throws IOException;

}