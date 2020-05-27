package cab302.database.user;

import javax.swing.*;

/**
 * This version uses an UserDataSource and its methods to retrieve data
 */
public class UserData {
    DefaultListModel listModel;

    UserDataSource userData;

    /**
     * Constructor initializes the list model that holds usernames as Strings and
     * attempts to read any data saved from previous invocations of the
     * application.
     */
    public UserData() {
        listModel = new DefaultListModel();
        userData = new UserDataSource();

        // add the retrieved data to the list model
        for (String username : userData.nameSet()) {
            listModel.addElement(username);
        }
    }

    /**
     * Adds a user to the users table.
     *
     * @param u A User to add to the users table.
     */
    public void add(UserInfo u) {

        // check to see if the user is already in the list model
        // if not add to the user table and the list model
        if (!listModel.contains(u.getUsername())) {
            listModel.addElement(u.getUsername());
            userData.addUser(u);
        }
    }

    /**
     * Based on the name of the user in the users table, delete the user.
     *
     * @param key
     */
    public void remove(Object key) {

        // remove from both list and map
        listModel.removeElement(key);
        userData.deleteUser((String) key);
    }

    /**
     * Retrieves user details from the model.
     *
     * @param key the username to retrieve.
     * @return the user object related to the name.
     */
    public UserInfo get(Object key) {
        return userData.getUser((String) key);
    }

    /**
     * Accessor for the list model.
     *
     * @return the listModel to display.
     */
    public ListModel getModel() {
        return listModel;
    }

    /**
     * @return the number of users in the users table.
     */
    public int getSize() {
        return userData.getSize();
    }

    /**
     * @param name               the user's name
     * @param username           the user's username
     * @param password           the user's password
     * @param salt               the user's salt string
     * @param email              the user's email
     * @param previousUsername   the user's username before edited
     * @param createBillboards   the user's createBillboards permission
     * @param editAllBillboards  the user's editAllBillboards permission
     * @param scheduleBillboards the user's scheduleBillboards permission
     * @param editUsers          the user's editUsers permission
     */
    public void edit(String name, String username, String password, String salt, String email, String previousUsername,
                     String createBillboards, String editAllBillboards,
                     String scheduleBillboards, String editUsers) {
        userData.editUser(name, username, password, salt, email, previousUsername, createBillboards, editAllBillboards,
                scheduleBillboards, editUsers);

    }
}
