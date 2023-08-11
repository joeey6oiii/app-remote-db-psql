package clientModules.authentication;

import token.Token;

public class User {
    private static User user;
    private Token<?> token;

    public static User getInstance() {
        if (user == null) {
            return new User();
        }
        return user;
    }

    public void setToken(Token<?> token) {
        this.token = token;
    }

    public Token<?> getToken() {
        return token;
    }
}
