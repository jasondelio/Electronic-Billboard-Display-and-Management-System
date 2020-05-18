/**
 * The Code for basic of our billboard source.
 * Unsure it will be worked well or not.
 */

package cab302.database.user;

import java.io.Serializable;

/**
 * Stores information of users
 */
public class UserInfo implements Comparable<UserInfo>, Serializable {

    private static final long serialVersionUID = -7092701502990374424L;

    private String name;

    private String salt;

    private String username;

    private String passwords;

    private String createBillboards;

    private String editAllBillboards;

    private String scheduleBillboards;

    private String editUsers;


    /**
     *
     */
    public UserInfo() {
    }

    /**
     *
     */
    public UserInfo(String name, String username, String passwords, String salt, String createBillboards,
                String editAllBillboards, String scheduleBillboards, String editUsers) {
        this.name = name;
        this.username = username;
        this.salt = salt;
        this.passwords = passwords;
        this.createBillboards = createBillboards;
        this.editAllBillboards = editAllBillboards;
        this.scheduleBillboards = scheduleBillboards;
        this.editUsers = editUsers;
    }

    /**
     *
     */
    public String getSalt() {
        return salt;
    }

    /**
     *
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     *
     */
    public String getName() {
        return name;
    }

    /**
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     */
    public String getPasswords() {
        return passwords;
    }

    /**
     *
     */
    public void setCreateBillboards(String createBillboards) {
        this.createBillboards = createBillboards;
    }

    /**
     *
     */
    public String getCreateBillboards() {
        return createBillboards;
    }

    /**
     *
     */
    public void setEditAllBillboards(String editAllBillboards) {
        this.editAllBillboards = editAllBillboards;
    }

    /**
     *
     */
    public String getEditAllBillboards() {
        return editAllBillboards;
    }

    /**
     *
     */
    public void setScheduleBillboards(String scheduleBillboards) {
        this.scheduleBillboards = scheduleBillboards;
    }

    /**
     *
     */
    public String getScheduleBillboards() {
        return scheduleBillboards;
    }

    /**
     *
     */
    public void setEditUsers(String editUsers) {
        this.editUsers = editUsers;
    }

    /**
     *
     */
    public String getEditUsers() {
        return editUsers;
    }

    /**
     *
     */
    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }


    /**
     *
     */
    public int compareTo(UserInfo other) {
        return this.name.compareTo(other.name);
    }

    /**
     *
     */
    public String toString() {
        return name + ", " + username + ", " + passwords + ", " + salt;
    }

}