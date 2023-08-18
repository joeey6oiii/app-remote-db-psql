package token;

import java.io.Serializable;
import java.util.Objects;

public class StringToken implements Token<String>, Serializable {
    private final String tokenValue;

    public StringToken(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getTokenValue() {
        return this.tokenValue;
    }

    @Override
    public String toString() {
        return "StringToken{" +
                "tokenValue='" + tokenValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringToken that = (StringToken) o;
        return Objects.equals(tokenValue, that.tokenValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenValue);
    }
}