package response.responses;

import token.Token;

public interface TokenResponse extends Response {

    Token<?> getToken();
}
