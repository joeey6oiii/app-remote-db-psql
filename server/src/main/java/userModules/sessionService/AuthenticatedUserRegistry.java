package userModules.sessionService;

import token.Token;
import userModules.users.AuthenticatedUser;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticatedUserRegistry {
    private static AuthenticatedUserRegistry singleInstance;
    private final ConcurrentHashMap<Token<?>, AuthenticatedUser> authenticatedUsers;
    private final ConcurrentHashMap<Integer, Token<?>> idTokenMap;

    {
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
            return false;
        }

        if(authenticatedUsers.putIfAbsent(token, authenticatedUser) == null) {
            if (idTokenMap.putIfAbsent(authenticatedUser.getId(), token) == null) {
                return true;
            } else {
                this.removeAuthenticatedUser(token);
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
            return;
        }

        AuthenticatedUser userToDel = this.getAuthenticatedUser(token);
        if (userToDel == null || userToDel.getId() == null) {
            return;
        }

        authenticatedUsers.remove(token);
        idTokenMap.remove(userToDel.getId());
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
