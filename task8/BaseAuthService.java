package task8;

import java.util.HashMap;
import java.util.Map;

public class BaseAuthService implements AuthService {

    private Map<String, User> users = new HashMap<>();

    public BaseAuthService() {
        users.put("alan", new User("alan", "password", "alan_1"));
        users.put("mike", new User("mikeXD", "password123", "mike_2"));
        users.put("ann", new User("ann_1", "passw0rd", "ann_n"));
    }

    @Override
    public String credentialsAuth(String login, String password) {
        for (User user : users.values()) {
            if (login.equals(user.getLogin()) && password.equals(user.getPassword()) && user.isActive())
                return user.getName();
        }
        return null;
    }

    @Override
    public User createOrActivateUser(String login, String password, String nick) {
        User user = new User(login, password, nick);
        if (users.containsKey(nick)) {
            users.get(nick).setActive(true);
            System.out.println("This name already exists");
        } else {
            users.put(nick, user);
            persist();
        }
        return user;
    }

    private void persist() {
        // TBD
    }

    @Override
    public boolean deactivateUser(String nick) {
        User user = users.get(nick);
        if (user != null) {
            user.setActive(false);
            return true;
        }
        return false;
    }
}
