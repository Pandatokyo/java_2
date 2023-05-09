package task8;

public interface AuthService {

    String credentialsAuth(String login, String password);

    User createOrActivateUser(String login, String password, String nick);

    boolean deactivateUser(String nick);
}
