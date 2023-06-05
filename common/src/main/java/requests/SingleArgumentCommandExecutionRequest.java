package requests;

import commands.CommandDescription;

/**
 * A class that represents the client command with an uncommon argument execution request.
 */

public class SingleArgumentCommandExecutionRequest<T> extends CommandExecutionRequest {
    private final T arg;

    /**
     * A constructor for a client command with an uncommon argument execution request object.
     *
     * @param command {@link CommandDescription} object by which the original command will be created
     * @param args arguments of the command
     * @param arg uncommon argument of the command
     */

    public SingleArgumentCommandExecutionRequest(CommandDescription command, String[] args, T arg) {
        super(command, args);
        this.arg = arg;
    }

    /**
     * A method that returns the uncommon argument of the command.
     */

    public T getArg() {
        return this.arg;
    }
}
