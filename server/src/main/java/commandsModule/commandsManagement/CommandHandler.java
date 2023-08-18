package commandsModule.commandsManagement;

public interface CommandHandler<T> {

    void executeCommand(T executable);
}
