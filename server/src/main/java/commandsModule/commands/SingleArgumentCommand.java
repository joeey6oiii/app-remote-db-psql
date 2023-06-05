package commandsModule.commands;

/**
 * An interface for all command-implementers with uncommon arguments.
 *
 * @param <T> uncommon argument of the command
 */

public interface SingleArgumentCommand<T> extends BaseCommand {

    /**
     * A method that returns the T argument of the command.
     */

    T getSingleArgument();

    /**
     * A method that sets the T argument to the command.
     */

    void setSingleArgument(T argument);

}
