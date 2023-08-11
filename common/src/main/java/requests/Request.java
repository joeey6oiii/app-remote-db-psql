package requests;

import token.Token;

/**
 * An interface for all requests-implementers.
 */
public interface Request {

    Token<?> getToken();
}