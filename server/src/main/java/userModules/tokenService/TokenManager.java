package userModules.tokenService;

import utility.Token;

/**
 * An abstraction for every TokenManager implementation.
 */
public interface TokenManager<T extends Token> {

    /**
     * @return
     */
    T generateToken(); // чота тут запутано все, надо либо попросче (не выпендриваться), либо по другому
}