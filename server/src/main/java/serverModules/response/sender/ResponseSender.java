package serverModules.response.sender;

import response.responses.Response;
import userModules.users.AbstractUser;

/**
 * An interface for all response sender-implementers.
 */
public interface ResponseSender {

    /**
     * A method that sends response to the client.
     *
     * @param user client
     * @param response answer to the client
     */
    void sendResponse(AbstractUser user, Response response);
}