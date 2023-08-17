package userModules.users;

import commandsModule.commands.BaseCommand;
import userModules.sessionService.Session;

import java.util.List;

public class AuthenticatedUser extends RegisteredUser {
    private final RegisteredUser registeredUser;
    private List<BaseCommand> commandHistory;
//    private final Session session;

    public AuthenticatedUser(RegisteredUser registeredUser) {
        super(registeredUser.getRegisteredUserData(), registeredUser.getUser());
        this.registeredUser = registeredUser;
    }

    public RegisteredUser getRegisteredUser() {
        return this.registeredUser;
    }

    public List<BaseCommand> getCommandHistory() {
        return this.commandHistory;
    }

    public void addCommandToHistory(BaseCommand command) {
        this.commandHistory.add(command);
    }

    // todo toStr equals hashCode
}