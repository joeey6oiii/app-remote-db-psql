package requests;


import java.io.Serializable;

/**
 * A class that represents a client request to receive the list of {@link commands.CommandDescription} objects from server.
 */

public class ClientCommandsRequest implements Request, Serializable {

    @Override
    public String getLogin() {
        return "no login for commands request";
    }

    @Override
    public char[] getPassword() {
        return new char[]{'n', 'o', ' ', 'p', 'a', 's', 's', 'w', 'o', 'r', 'd', ' ', 'f', 'o', 'r',
                ' ', 'c', 'o', 'm', 'm', 'a', 'n', 'd', 's', ' ', 'r', 'e', 'q', 'u', 'e', 's', 't'};
    }

}
