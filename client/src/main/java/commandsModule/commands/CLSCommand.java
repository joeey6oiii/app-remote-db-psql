package commandsModule.commands;

import commandsModule.commandsManagement.CommandHandler;

public class CLSCommand implements ClientCommand {

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
        if (CommandHandler.getMissedCommandsMap().isEmpty()) {
            System.out.println("Cache of missed commands is empty");
        } else {
            CommandHandler.getMissedCommandsMap().clear();
            System.out.println("Cleared cache of missed commands");
        }
    }
}
