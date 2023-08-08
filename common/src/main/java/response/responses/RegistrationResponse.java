package response.responses;

import utility.Token;

import java.io.Serializable;

public class RegistrationResponse implements Response, Serializable {
    private final boolean success;
    private final Token token;
    private final String result;

    public RegistrationResponse(boolean success,Token token, String result) {
        this.success = success;
        this.token = token;
        this.result = result;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Token getToken() {
        return this.token;
    }

    public String getResult() {
        return this.result;
    }
}