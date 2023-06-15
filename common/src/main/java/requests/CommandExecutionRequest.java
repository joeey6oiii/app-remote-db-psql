package requests;

import commands.CommandDescription;

import java.io.Serializable;

/**
 * A class that represents the client command execution request.
 */

public class CommandExecutionRequest implements Request, Serializable {
    private final String login;
    private final char[] passwd;
    private final CommandDescription command;
    private final String[] args;

    /**
     * A constructor for a client command execution request object.
     *
     * @param command {@link CommandDescription} object by which the original command will be created
     * @param args arguments of the command
     */

    public CommandExecutionRequest(String login, char[] passwd, CommandDescription command, String[] args) {
        this.login = login;
        this.passwd = passwd;
        this.command = command;
        this.args = args;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public char[] getPassword() {
        return passwd;
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
