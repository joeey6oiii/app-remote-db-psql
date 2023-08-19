package clientModules.authentication;

import token.Token;

public class User {
    private static User user;
    private Token<?> token;

    private User() {}

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setToken(Token<?> token) {
        this.token = token;
    }

    public Token<?> getToken() {
        return this.token;
    }
}
