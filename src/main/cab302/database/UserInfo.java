/**
 * The Code for basic of our billboard source.
 * Unsure it will be worked well or not.
 */

package cab302.database.user;

import java.io.*;

/**
 * Stores information of users
 */
public class UserInfo implements Comparable<UserInfo>, Serializable {

    private static final long serialVersionUID = -7092701502990374424L;

    private String name;

    private String email;

    private String id;

    private String passwords;


    /**
     */
    public UserInfo() {
    }

    /**
     */
    public UserInfo(String name, String id, String passwords, String email) {
        this.name = name;
        this.id= id;
        this.email = email;
        this.passwords= passwords;
    }

    /**
     */
    public String getEmail() {
        return email;
    }

    /**
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     */
    public String getName() {
        return name;
    }

    /**
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     */
    public String getId() {
        return id;
    }

    /**
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     */
    public String getPasswords() {
        return passwords;
    }

    /**
     */
    public void setPasswords(String passwords) {
        this.passwords = passwords;
    }

    /**
     */
    public int compareTo(UserInfo other) {
        return this.name.compareTo(other.name);
    }

    /**
     */
    public String toString() {
        return name + " " + id + ", " + passwords + " " + email;
    }

}
