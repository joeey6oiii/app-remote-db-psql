package commandsModule.commands;

/**
 * An interface for all command-implementers with uncommon arguments.
 *
 * @param <T> uncommon argument of the command
 */
public interface ObjectArgumentCommand<T> extends BaseCommand {

    /**
     * A method that returns the T argument of the command.
     */
    T getObjArgument();

    /**
     * A method that sets the T argument to the command.
     */
    void setObjArgument(T argument);
}
