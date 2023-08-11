package token;

import java.io.Serializable;

public class StringToken implements Token<String>, Serializable {
    private final String tokenValue;

    public StringToken(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getTokenValue() {
        return this.tokenValue;
    }
}