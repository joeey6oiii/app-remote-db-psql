package requests;

public class RegistrationRequest implements Request {
    private final String login;
    private final char[] passwd;

    public RegistrationRequest(String login, char[] passwd) {
        this.login = login;
        this.passwd = passwd;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public char[] getPassword() {
        return passwd;
    }

}
