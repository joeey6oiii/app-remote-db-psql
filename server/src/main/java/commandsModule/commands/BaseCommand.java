package commandsModule.commands;

import commands.CommandType;

import java.io.IOException;

/**
 * An interface for all command-implementers without arguments.
 */
public interface BaseCommand {

    /**
     * @return the name of the command
     */
    String getName();

    /**
     * @return the response of the command
     */
    String getResponse();

    /**
     * @return the type of command
     */
    CommandType getType();

    /**
     * @return the description of the command
     */
    String describe();

    /**
     * Command call method.
     *
     * @throws IOException when failed during I/O operations
     */
    void execute() throws IOException;
}