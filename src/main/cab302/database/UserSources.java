
package cab302.database.user;

import java.util.*;

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
    UserInfo editUser(String name);

    /**
     */
    void close();

    /**
     */
    Set<String> nameSet();

}