package requests;

import token.Token;

public interface TokenRequest extends Request {

    Token<?> getToken();
}
