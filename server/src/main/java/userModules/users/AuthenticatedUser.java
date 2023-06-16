package userModules.users;

import java.util.Objects;

public class AuthenticatedUser extends User {
    private final AuthenticatedUserData authenticatedUserData;

    public AuthenticatedUser(AuthenticatedUserData authenticatedUserData, User user) {
        super(user.getAddress(), user.getPort());
        this.authenticatedUserData = authenticatedUserData;
    }

    public AuthenticatedUserData getAuthenticatedUserData() {
        return this.authenticatedUserData;
    }

    @Override
    public String toString() {
        return "AuthenticatedUser{" +
                "authenticatedUserData=" + authenticatedUserData +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticatedUser that = (AuthenticatedUser) o;
        return Objects.equals(authenticatedUserData, that.authenticatedUserData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticatedUserData);
    }

}
