package serverModules.response.sender;

import userModules.users.User;
import serverModules.connection.ConnectionModule;

/**
 * An interface for all response sender-implementers.
 *
 * @param <T> concrete response
 */
public interface ResponseAble<T> {

    /**
     * A method that sends response of a T type to the client.
     *
     * @param connectionModule server core
     * @param user client
     * @param response answer to the client
     */
    void sendResponse(ConnectionModule connectionModule, User user, T response);
}