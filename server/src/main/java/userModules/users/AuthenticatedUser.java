package userModules.users;

public class AuthenticatedUser extends RegisteredUser {
    private final RegisteredUser registeredUser;
    // todo session

    public AuthenticatedUser(RegisteredUser registeredUser) {
        super(registeredUser.getRegisteredUserData(), registeredUser.getUser());
        this.registeredUser = registeredUser;
    }

    public RegisteredUser getRegisteredUser() {
        return this.registeredUser;
    }

    // todo toStr equals hashCode
}
