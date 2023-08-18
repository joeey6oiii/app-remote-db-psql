package commandsModule.commandReceivers;

import commands.CommandDescription;

/**
 * An interface for all command receiver-implementers.
 */
public interface CommandReceiver {

    /**
     * A method that receives commands to continue sending and receiving operations.
     *
     * @param command simplified command
     * @param args simplified command arguments
     */
    void receiveCommand(CommandDescription command, String[] args);
}