package serverModules.users;

public class User {
    private final String login;
    private final byte[] passwd;
    private final byte[] salt;

    public User(String login, byte[] passwd, byte[] salt) {
        this.login = login;
        this.passwd = passwd;
        this.salt = salt;
    }

    public String getLogin() {
        return this.login;
    }

    public byte[] getPasswd() {
        return this.passwd;
    }

    public byte[] getSalt() {
        return this.salt;
    }

}
