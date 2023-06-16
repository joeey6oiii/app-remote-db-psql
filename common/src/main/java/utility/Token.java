package utility;

import java.io.Serializable;

public class Token implements Serializable {
    private final String token;

    public Token(String token) {
        this.token = token;
    }

    public String getTokenValue() {
        return this.token;
    }

}
