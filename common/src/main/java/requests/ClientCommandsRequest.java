package requests;

import utility.Token;

import java.io.Serializable;

/**
 * A class that represents a client request to receive the list of {@link commands.CommandDescription} objects from server.
 */

public class ClientCommandsRequest implements Request, Serializable {
    private final Token token = new Token("default_token_val");

    @Override
    public Token getToken() {
        return this.token;
    }

}
