package userModules.tokenService;

import token.Token;

/**
 * An abstraction for every TokenManager implementation.
 */
public interface TokenManager<T> {

    /**
     * @return generated token
     */
    Token<T> generateToken();
}