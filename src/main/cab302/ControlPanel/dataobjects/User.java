package cab302.controlpanel.dataobjects;

import java.io.*;

/**
 * Stores information of users
 */
public class User implements Comparable<User>, Serializable {

    private static final long serialVersionUID = -7092701502990374424L;

    private String name;

    private String email;

    private String username;

    private String passwords;


    /**
     */
    public User() {
    }

    /**
     */
    public User(String name, String username, String passwords, String email) {
        this.name = name;
        this.username = username;
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
    public String getUsername() {
        return username;
    }

    /**
     */
    public void setUsername(String username) {
        this.username = username;
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
    public int compareTo(User other) {
        return this.name.compareTo(other.name);
    }

    /**
     */
    public String toString() {
        return name + ", " + username + ", " + passwords + ", " + email;
    }

}