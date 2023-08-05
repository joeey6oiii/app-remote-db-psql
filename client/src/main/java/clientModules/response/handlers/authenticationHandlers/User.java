package clientModules.response.handlers.authenticationHandlers;

import utility.Token;

public class User {
    private static Token token;

    protected static void setToken(Token token) {
        User.token = token;
    }

    public static Token getToken() {
        return token;
    }
}
