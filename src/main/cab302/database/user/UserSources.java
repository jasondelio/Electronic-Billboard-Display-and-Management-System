
package cab302.database.user;

import java.util.Set;

public interface UserSources {

    /**
     * Add a user to the users table in database
     *
     * @param u User to add
     */
    void addUser(UserInfo u);

    /**
     * Extracts all the details of a user from the users table based on the
     * username passed in.
     *
     * @param username the user's username which want to be extracted
     * @return
     */
    UserInfo getUser(String username);

    /**
     */
    int getSize();

    /**
     * Deletes a user from the users table
     *
     * @param username the user's username to delete from users table
     */
    void deleteUser(String username);

    /**
     * @param name               The user's name after edited
     * @param username           The user's username after edited
     * @param password           The user's password after edited
     * @param salt               The user's salt string after edited
     * @param email              The user's email after edited
     * @param previousUsername   The user's username before edited
     * @param createBillboards   The user's createBillboards permission after edited
     * @param editAllBillboards  The user's editAllBillboards permission after edited
     * @param scheduleBillboards The user's scheduleBillboards permission after edited
     * @param editUsers          The user's editUsers permission after edited
     */
    void editUser(String name, String username, String password, String salt, String email, String previousUsername,
                  String createBillboards, String editAllBillboards,
                  String scheduleBillboards, String editUsers);

    /**
     *
     */
    void close();

    /**
     * Retrieves a set of  users username from the data source that are used in
     * the users username list.
     *
     * @return set of users username
     */
    Set<String> nameSet();


}