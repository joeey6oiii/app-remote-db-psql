package userModules.sessionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import token.Token;
import userModules.users.AuthenticatedUser;
import utils.UserUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticatedUserRegistry {
    private static final Logger logger = LogManager.getLogger("logger.AuthenticatedUserRegistry");
    private static AuthenticatedUserRegistry singleInstance;
    private final ConcurrentHashMap<Token<?>, AuthenticatedUser> authenticatedUsers;
    private final ConcurrentHashMap<Integer, Token<?>> idTokenMap;

    private AuthenticatedUserRegistry() {
        authenticatedUsers = new ConcurrentHashMap<>();
        idTokenMap = new ConcurrentHashMap<>();
    }

    /**
     * @return the singleton instance of the AuthenticatedUserRegistry class
     */
    public static AuthenticatedUserRegistry getInstance() {
        if (singleInstance == null) {
            singleInstance = new AuthenticatedUserRegistry();
        }
        return singleInstance;
    }

    /**
     * Returns the entry set of authenticated users.
     *
     * @param  <K>  the type of keys maintained by the map
     * @param  <V>  the type of mapped values
     * @return      the entry set of authenticated users
     */
    protected  <K, V> Set<Map.Entry<Token<?>, AuthenticatedUser>> getEntrySet() {
        return authenticatedUsers.entrySet();
    }

    /**
     * Adds an authenticated user to the collection of authenticated users.
     *
     * @param  token                the token associated with the authenticated user
     * @param  authenticatedUser    the authenticated user to be added
     * @return                      true if the user was added successfully, false otherwise
     */
    public boolean addAuthenticatedUser(Token<?> token, AuthenticatedUser authenticatedUser) {
        if (token == null || token.getTokenValue() == null ||
                authenticatedUser == null || authenticatedUser.getId() == null) {
            logger.error("Could not add authenticated user. Received invalid token");
            return false;
        }

        if(authenticatedUsers.putIfAbsent(token, authenticatedUser) == null) {
            if (idTokenMap.putIfAbsent(authenticatedUser.getId(), token) == null) {
                logger.info("Opened session for user with id " + authenticatedUser.getId() +
                        ". Timeout: " + UserUtils.INSTANCE.getSessionDurationInMinutes() + " minutes");
                return true;
            } else {
                logger.error("Successfully added user to authenticatedUsers collection but failed to add user to the idToken collection");
                this.removeAuthenticatedUser(token);
                logger.info("Removed user from authenticatedUsers collection");
            }
        }

        return false;
    }

    /**
     * Retrieves the authenticated user associated with the given token.
     *
     * @param  token  the token used to authenticate the user
     * @return        the authenticated user associated with the token
     */
    public AuthenticatedUser getAuthenticatedUser(Token<?> token) {
        if (token == null) {
            logger.error("Could not retrieve authenticated user. Received invalid token");
            return null;
        }

        return authenticatedUsers.get(token);
    }

    /**
     * Retrieves the authenticated user with the specified ID.
     *
     * @param  id  the ID of the user to retrieve
     * @return     the authenticated user with the specified ID, or null if no user is found
     */
    public AuthenticatedUser getAuthenticatedUser(int id) {
        return this.getAuthenticatedUser(idTokenMap.get(id));
    }

    /**
     * Removes an authenticated user.
     *
     * @param  token  the token of the user to be removed
     */
    public void removeAuthenticatedUser(Token<?> token) {
        if (token == null || token.getTokenValue() == null) {
            logger.error("Could not end session for user. Received invalid token");
            return;
        }

        AuthenticatedUser userToDel = this.getAuthenticatedUser(token);
        if (userToDel == null || userToDel.getId() == null) {
            logger.error("Could not end session for user. User does not exist");
            return;
        }

        authenticatedUsers.remove(token);
        idTokenMap.remove(userToDel.getId());
        logger.info("Closed session for user with id " + userToDel.getId());
    }

    /**
     * Removes an authenticated user with the specified ID.
     *
     * @param  id  the ID of the user to be removed
     */
    public void removeAuthenticatedUser(int id) {
        this.removeAuthenticatedUser(idTokenMap.get(id));
    }

    /**
     * Checks if the given authenticated user exists in the collection of authenticated users.
     *
     * @param  authenticatedUser  the authenticated user to be checked
     * @return       true if the user exists, false otherwise
     */
    public boolean checkUserExistence(AuthenticatedUser authenticatedUser) {
        return this.getAuthenticatedUser(authenticatedUser.getId()) != null;
    }
}
