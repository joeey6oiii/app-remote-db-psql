package commandsModule.commands;

import commandsModule.commandsManagement.CommandHandler;

public class CLSCommand implements OfflineCommand {

    @Override
    public String getName() {
        return "cls";
    }

    @Override
    public String getDescription() {
        return "Clears the cache of commands which were not executed by the server";
    }

    @Override
    public void execute() {
        CommandHandler.getMissedCommandsMap().clear();
        System.out.println("Cleared cache of missed commands");
    }
}
