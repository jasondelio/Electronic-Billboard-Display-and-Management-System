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

    private String createBillboards;

    private String editAllBillboards;

    private String scheduleBillboards;

    private String editUsers;


    /**
     */
    public User() {
    }

    /**
     */
    public User(String name, String username, String passwords, String email, String createBillboards,
                String editAllBillboards, String scheduleBillboards, String editUsers) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.passwords= passwords;
        this.createBillboards = createBillboards;
        this.editAllBillboards = editAllBillboards;
        this.scheduleBillboards = scheduleBillboards;
        this.editUsers = editUsers;
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
    public void setCreateBillboards(String createBillboards) {
        this.createBillboards = createBillboards;
    }

    /**
     */
    public String getCreateBillboards() {
        return createBillboards;
    }

    /**
     */
    public void setEditAllBillboards(String editAllBillboards) {
        this.editAllBillboards = editAllBillboards;
    }

    /**
     */
    public String getEditAllBillboards() {
        return editAllBillboards;
    }

    /**
     */
    public void setScheduleBillboards(String scheduleBillboards) {
        this.scheduleBillboards = scheduleBillboards;
    }

    /**
     */
    public String getScheduleBillboards() {
        return scheduleBillboards;
    }

    /**
     */
    public void setEditUsers(String editUsers) {
        this.editUsers = editUsers;
    }

    /**
     */
    public String getEditUsers() {
        return editUsers;
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