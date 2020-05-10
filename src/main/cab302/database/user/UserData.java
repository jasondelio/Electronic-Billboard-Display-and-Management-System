package cab302.database.user;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

public class UserData {


    DefaultListModel listModel;

    UserDataSource userData;


    public UserData() {
        listModel = new DefaultListModel();
        userData = new UserDataSource();
        for (String name : userData.nameSet()) {
            listModel.addElement(name);
        }
    }

    /**
     */
    public void add(UserInfo u) {

        if (!listModel.contains(u.getId())) {
            listModel.addElement(u.getId());
            userData.addUser(u);
        }
    }

    /**
     */
    public void remove(Object key) {

        // remove from both list and map
        listModel.removeElement(key);
        userData.deleteUser((String) key);
    }

    /**
     */
    public void persist() {
        userData.close();
    }

    /**
     */
    public UserInfo get(Object key) {
        return userData.getUser((String) key);
    }

    /**
     */
    public ListModel getModel() {
        return listModel;
    }

    /**
     */
    public int getSize() {
        return userData.getSize();
    }
}


