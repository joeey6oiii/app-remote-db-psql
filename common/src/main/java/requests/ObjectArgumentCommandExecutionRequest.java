package requests;

import commands.CommandDescription;
import token.StringToken;
import token.Token;

/**
 * A class that represents the client command with an uncommon argument execution request.
 */
public class ObjectArgumentCommandExecutionRequest<T> extends CommandExecutionRequest {
    private final T arg;

    /**
     * A constructor for a client command with an object argument execution request object.
     *
     * @param command {@link CommandDescription} object by which the original command will be created.
     * @param args arguments of the command.
     * @param arg object argument of the command.
     */
    public ObjectArgumentCommandExecutionRequest(Token<?> token, CommandDescription command, String[] args, T arg) {
        super(token, command, args);
        this.arg = arg;
    }

    /**
     * A method that returns the object argument of the command.
     */
    public T getObjArg() {
        return this.arg;
    }
}