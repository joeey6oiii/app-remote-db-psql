package requests;

import token.Token;

import java.io.Serializable;

/**
 * A class that represents a client request to receive the list of {@link commands.CommandDescription} objects from server.
 */
public class ClientCommandsRequest implements Request, Serializable {
    private Token<?> token;

    @Override
    public Token<?> getToken() {
        return this.token;
    }
}