package clientModules.response.receivers;

import clientModules.connection.DataTransferConnectionModule;
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
     * @param module client core
     */

    void receiveCommand(CommandDescription command, String[] args, DataTransferConnectionModule module);

}
