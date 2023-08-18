package response.responses;

import response.visitor.ResponseVisitor;
import token.Token;

import java.io.Serializable;

public class AuthorizationResponse implements TokenResponse, Serializable {
    private final boolean success;
    private final Token<?> token;
    private final String result;

    public AuthorizationResponse(boolean success, Token<?> token, String result) {
        this.success = success;
        this.token = token;
        this.result = result;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Token<?> getToken() {
        return this.token;
    }

    public String getResult() {
        return this.result;
    }

    @Override
    public boolean accept(ResponseVisitor visitor) {
        return visitor.visit(this);
    }
}