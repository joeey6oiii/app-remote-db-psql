package response.data;

import java.io.Serializable;

public class AuthenticationData implements Serializable {
    private final String login;
    private final char[] passwd;

    public AuthenticationData(String login, char[] passwd) {
        this.login = login;
        this.passwd = passwd;
    }

    public String getLogin() {
        return login;
    }

    public char[] getPassword() {
        return passwd;
    }
}