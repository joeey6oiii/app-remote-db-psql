package userModules.users;

import response.data.AuthenticationData;

public class User {
    private final AuthenticationData authenticationData;
    private final byte[] salt;

    public User(AuthenticationData authenticationData, byte[] salt) {
        this.authenticationData = authenticationData;
        this.salt = salt;
    }

    public AuthenticationData getAuthenticationData() {
        return this.authenticationData;
    }

    public byte[] getSalt() {
        return this.salt;
    }

}
