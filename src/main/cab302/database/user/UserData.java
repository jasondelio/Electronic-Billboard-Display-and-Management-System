package cab302.database.user;

import javax.swing.*;
import java.util.Set;

public class UserData {
    DefaultListModel listModel;

    UserDataSource userData;

    /**
     * Constructor initializes the list model that holds names as Strings and
     * attempts to read any data saved from previous invocations of the
     * application.
     *
     */
    public UserData() {
        listModel = new DefaultListModel();
        /* BEGIN MISSING CODE */
        userData = new UserDataSource();
        /* END MISSING CODE */

        // add the retrieved data to the list model
        for (String username : userData.nameSet()) {
            listModel.addElement(username);
        }
    }

    /**
     * Adds a person to the address book.
     *
     * @param u A User to add to the address book.
     */
    public void add(UserInfo u) {

        // check to see if the person is already in the book
        // if not add to the address book and the list model
        if (!listModel.contains(u.getUsername())) {
            listModel.addElement(u.getUsername());
            userData.addUser(u);
        }
    }

    /**
     * Based on the name of the person in the address book, delete the person.
     *
     * @param key
     */
    public void remove(Object key) {

        // remove from both list and map
        listModel.removeElement(key);
        userData.deleteUser((String) key);
    }

    /**
     * Saves the data in the address book using a persistence
     * mechanism.
     */
    public void persist() {
        userData.close();
    }

    /**
     * Retrieves Person details from the model.
     *
     * @param key the name to retrieve.
     * @return the Person object related to the name.
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
     * @return the number of names in the Address Book.
     */
    public int getSize() {
        return userData.getSize();
    }

    public boolean isValidUser(String username, String password)
    {
        return userData.isValidUser(username, password);
    }
    public void edit(String name, String username, String password, String salt, String email, String previousUsername,
                     String createBillboards, String editAllBillboards,
                     String scheduleBillboards, String editUsers)
    {
        userData.editUser(name, username, password, salt,email, previousUsername, createBillboards, editAllBillboards,
                scheduleBillboards, editUsers);

    }
    public Set<String> getUserList() { return  userData.nameSet(); }
}
