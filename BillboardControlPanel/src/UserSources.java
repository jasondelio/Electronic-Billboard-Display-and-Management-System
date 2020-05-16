import java.util.*;

public interface UserSources {

    /**
     */
    void addUser(User u);

    /**
     */
    User getUser(String name);

    /**
     */
    int getSize();

    /**
     */
    void deleteUser(String name);

    /**
     *
     * @return
     */
    void editUser(String name, String username, String password, String email, String previousUsername,
                  String createBillboards, String editAllBillboards,
                  String scheduleBillboards, String editUsers);

    /**
     */
    void close();

    /**
     */
    Set<String> nameSet();

    boolean isValidUser(String username, String Password);

}