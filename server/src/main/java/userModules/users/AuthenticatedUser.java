package userModules.users;

import commandsModule.commands.BaseCommand;
import userModules.sessionService.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthenticatedUser extends RegisteredUser {
    private final List<BaseCommand> commandHistory;
    private final Session session;

    public AuthenticatedUser(RegisteredUser registeredUser, Session session) {
        super(registeredUser.getRegisteredUserData(), new User(registeredUser.getAddress(), registeredUser.getPort()));
        this.setId(registeredUser.getId());
        this.commandHistory = new ArrayList<>();
        this.session = session;
    }

    public List<BaseCommand> getCommandHistory() {
        return this.commandHistory;
    }

    public void addCommandToHistory(BaseCommand command) {
        this.commandHistory.add(command);
    }

    public Session getSession() {
        return this.session;
    }

    @Override
    public String toString() {
        return "AuthenticatedUser{" +
                "commandHistory=" + commandHistory +
                ", session=" + session +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AuthenticatedUser that = (AuthenticatedUser) o;
        return Objects.equals(commandHistory, that.commandHistory) && Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), commandHistory, session);
    }
}