package serverModules.users;

import java.util.HashMap;
import java.util.Map;

public class UserRegistry {
    private Map<String, User> users;

    public UserRegistry() {
        users = new HashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getLogin(), user);
    }

    public User getUserByLogin(String login) {
        return users.get(login);
    }

}

