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

    public void addAuthenticatedUser(Token<?> token, AuthenticatedUser authenticatedUser) {
        authenticatedUsers.put(token, authenticatedUser);
    }

    public AuthenticatedUser getAuthenticatedUser(Token<?> token) {
        return authenticatedUsers.get(token);
    }

    public AuthenticatedUser getAuthenticatedUser(int id) {
        for (AuthenticatedUser user : authenticatedUsers.values()) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public void removeAuthenticatedUser(Token<?> token) {
        authenticatedUsers.remove(token);
    }

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
}
