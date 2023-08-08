package requests;

import commands.CommandDescription;
import utility.Token;

import java.io.Serializable;

/**
 * A class that represents the client command execution request.
 */
public class CommandExecutionRequest implements Request, Serializable {
    private final Token token;
    private final CommandDescription command;
    private final String[] args;

    /**
     * A constructor for a client command execution request object.
     *
     * @param command {@link CommandDescription} object by which the original command will be created
     * @param args arguments of the command
     */
    public CommandExecutionRequest(Token token, CommandDescription command, String[] args) {
        this.token = token;
        this.command = command;
        this.args = args;
    }

    @Override
    public Token getToken() {
        return this.token;
    }

    /**
     * A method that returns the contained {@link CommandDescription} object.
     */
    public CommandDescription getDescriptionCommand() {
        return this.command;
    }

    /**
     * A method that returns the contained arguments for the {@link CommandDescription} object.
     */
    public String[] getArgs() {
        return this.args;
    }
}