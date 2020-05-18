
package cab302.database.user;

import java.util.Set;

public interface UserSources {

    /**
     */
    void addUser(UserInfo u);

    /**
     */
    UserInfo getUser(String name);

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
    void editUser(String name, String username, String password, String salt, String previousUsername,
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