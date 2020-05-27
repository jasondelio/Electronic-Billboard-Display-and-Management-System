/**
 * The Code for basic of our billboard source.
 * Unsure it will be worked well or not.
 */

package cab302.database.user;

import java.io.Serializable;

/**
 * Stores the user details for a user
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -7092701502990374424L;

    private String name;

    private String salt;

    private String username;

    private String passwords;

    private String email;

    private String createBillboards;

    private String editAllBillboards;

    private String scheduleBillboards;

    private String editUsers;


    /**
     * No args constructor.
     */
    public UserInfo() {
    }

    /**
     * Constructor to set values for the user's details.
     *
     * @param name               user's name
     * @param username           user's username
     * @param passwords          user's password
     * @param salt               user's salt string
     * @param email              user's email
     * @param createBillboards   user's createBillboard permission
     * @param editAllBillboards  user's editAllBillboard permission
     * @param scheduleBillboards user's scheduleBillboards permission
     * @param editUsers          user's editUsers permission
     */
    public UserInfo(String name, String username, String passwords, String salt, String email, String createBillboards,
                String editAllBillboards, String scheduleBillboards, String editUsers) {
        this.name = name;
        this.username = username;
        this.salt = salt;
        this.email = email;
        this.passwords = passwords;
        this.createBillboards = createBillboards;
        this.editAllBillboards = editAllBillboards;
        this.scheduleBillboards = scheduleBillboards;
        this.editUsers = editUsers;
    }

    /**
     * @return the user's salt string
     */
    public String getSalt() {
        return salt;
    }

    /**
     *
     * @param salt the user's salt string to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name the user's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username the user's username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * @return the user's hashed salted password
     */
    public String getPasswords() {
        return passwords;
    }

    /**
     * @param passwords the user's hashed salted password to set
     */
    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    /**
     * @return the user's createBillboards permission
     */
    public String getCreateBillboards() {
        return createBillboards;
    }

    /**
     * @param createBillboards the user's createBillboards permission to set
     */
    public void setCreateBillboards(String createBillboards) {
        this.createBillboards = createBillboards;
    }

    /**
     * @return the user's editAllBillboards permission
     */
    public String getEditAllBillboards() {
        return editAllBillboards;
    }

    /**
     *
     * @param editAllBillboards the user's editAllBillboards permission to set
     */
    public void setEditAllBillboards(String editAllBillboards) {
        this.editAllBillboards = editAllBillboards;
    }

    /**
     * @return the user's scheduleBillboards permission
     */
    public String getScheduleBillboards() {
        return scheduleBillboards;
    }

    /**
     * @param scheduleBillboards the user's scheduleBillboards permission to set
     */
    public void setScheduleBillboards(String scheduleBillboards) {
        this.scheduleBillboards = scheduleBillboards;
    }

    /**
     * @return the user's editUsers permission
     */
    public String getEditUsers() {
        return editUsers;
    }

    /**
     * @param editUsers the user's editUsers permission to set
     */
    public void setEditUsers(String editUsers) {
        this.editUsers = editUsers;
    }

    /**
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email the user's email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return name + ", " + username + ", " + passwords + ", " + salt + ", " + email;
    }

}