package requests;

import response.data.AuthenticationData;
import token.Token;

import java.io.Serializable;

public class RegistrationRequest implements Request, Serializable {
    private final AuthenticationData authenticationData;
    private Token<?> stringToken;

    public RegistrationRequest(AuthenticationData authenticationData) {
        this.authenticationData = authenticationData;
    }

    public AuthenticationData getAuthenticationData() {
        return this.authenticationData;
    }

    @Override
    public Token<?> getToken() {
        return this.stringToken;
    }
}