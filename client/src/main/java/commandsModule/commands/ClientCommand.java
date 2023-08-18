package commandsModule.commands;

public interface ClientCommand {

    String getName();

    String getDescription();

    void execute();
}
