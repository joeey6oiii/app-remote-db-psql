package userModules.users;

import java.util.Objects;

public class RegisteredUser extends User {
    private final RegisteredUserData registeredUserData;
    private final User user;

    public RegisteredUser(RegisteredUserData registeredUserData, User user) {
        super(user.getAddress(), user.getPort());
        this.registeredUserData = registeredUserData;
        this.user = user;
    }

    public RegisteredUserData getRegisteredUserData() {
        return this.registeredUserData;
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public String toString() {
        return "RegisteredUser{" +
                "registeredUserData=" + registeredUserData +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisteredUser that = (RegisteredUser) o;
        return Objects.equals(registeredUserData, that.registeredUserData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registeredUserData);
    }
}