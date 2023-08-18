package userModules;

import token.Token;
import userModules.users.AuthenticatedUser;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticatedUserRegistry {
    private static AuthenticatedUserRegistry singleInstance;
    private final ConcurrentHashMap<Token<?>, AuthenticatedUser> authenticatedUsers;

    {
        authenticatedUsers = new ConcurrentHashMap<>();
    }

    public static AuthenticatedUserRegistry getInstance() {
        if (singleInstance == null) {
            singleInstance = new AuthenticatedUserRegistry();
        }
        return singleInstance;
    }

    public <K, V> Set<Map.Entry<Token<?>, AuthenticatedUser>> getEntrySet() {
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
        return authenticatedUsers.putIfAbsent(token, authenticatedUser) == null;
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
        for (AuthenticatedUser user : authenticatedUsers.values()) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    /**
     * Removes an authenticated user.
     *
     * @param  token  the token of the user to be removed
     */
    public void removeAuthenticatedUser(Token<?> token) {
        authenticatedUsers.remove(token);
    }

    /**
     * Removes an authenticated user with the specified ID.
     *
     * @param  id  the ID of the user to be removed
     */
    public void removeAuthenticatedUser(int id) {
        authenticatedUsers.forEach((token, user) -> {
            if (user.getId() == id) {
                authenticatedUsers.compute(token, (k, v) -> {
                    if (v != null && v.getId() == id) {
                        return null;
                    }
                    return v;
                });
            }
        });
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
