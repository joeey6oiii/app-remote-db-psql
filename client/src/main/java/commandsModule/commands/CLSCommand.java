package commandsModule.commands;

import commandsModule.commandsManagement.CommandHandler;
import outputService.ColoredPrintStream;
import outputService.OutputSource;

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
        ColoredPrintStream cps = new ColoredPrintStream(OutputSource.getOutputStream());

        if (CommandHandler.getMissedCommandsMap().isEmpty()) {
            cps.println("Cache of missed commands is empty");
        } else {
            CommandHandler.getMissedCommandsMap().clear();
            cps.println("Cleared cache of missed commands");
        }
    }
}
