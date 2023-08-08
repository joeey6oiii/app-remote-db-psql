package requests;

import response.data.AuthenticationData;
import utility.Token;

import java.io.Serializable;

public class RegistrationRequest implements Request, Serializable {
    private final AuthenticationData authenticationData;
    private final Token token = new Token("default_token_val");

    public RegistrationRequest(AuthenticationData authenticationData) {
        this.authenticationData = authenticationData;
    }

    public AuthenticationData getAuthenticationData() {
        return this.authenticationData;
    }

    @Override
    public Token getToken() {
        return this.token;
    }
}