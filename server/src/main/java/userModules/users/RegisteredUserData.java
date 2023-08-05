package userModules.users;

import userModules.passwordService.EncryptedPassword;

public class RegisteredUserData {
    private final String login;
    private final EncryptedPassword encryptedPassword;

    public RegisteredUserData(String login, EncryptedPassword encryptedPassword) {
        this.login = login;
        this.encryptedPassword = encryptedPassword;
    }

    public String getLogin() {
        return this.login;
    }

    public EncryptedPassword getEncryptedPassword() {
        return this.encryptedPassword;
    }
}
