/**
 * The Code for basic of our billboard source.
 * Unsure it will be worked well or not.
 */
package cab302.database.user;

import cab302.database.DataConnection;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;


/**
 * Class for retrieving data from users table in database.
 */
public class UserDataSource implements UserSources {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS users ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "name VARCHAR(30),"
                    + "username VARCHAR(30) UNIQUE,"
                    + "passwords VARCHAR(70),"
                    + "salt VARCHAR(70),"
                    + "email VARCHAR(70),"
                    + "createBillboards VARCHAR(5),"
                    + "editAllBillboards VARCHAR(5),"
                    + "scheduleBillboards VARCHAR(5),"
                    + "editUsers VARCHAR(5)" + ");";

    private static final String INSERT_USER = "INSERT INTO users (name, username, passwords, salt, email" +
            ", createBillboards, editAllBillboards, scheduleBillboards, editUsers) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String GET_USERNAMES = "SELECT username FROM users";

    private static final String GET_USER = "SELECT * FROM users WHERE username=?";

    private static final String DELETE_USER = "DELETE FROM users WHERE username=?";

    private static final String EDIT_USER = "UPDATE users SET name=?, username=?, passwords=?, salt=?, email=?" +
            ", createBillboards=?, editAllBillboards=?, scheduleBillboards=?, editUsers=? WHERE username=?";

    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM users";

    private static final String GET_VALID = "SELECT * FROM users WHERE username=? AND passwords =?";

    private Connection connection;

    private PreparedStatement addUser;

    private PreparedStatement getNameList;

    private PreparedStatement getUser;

    private PreparedStatement deleteUser;

    private PreparedStatement editUser;

    private PreparedStatement rowCount;

    private PreparedStatement isValidUser;


    public UserDataSource() {
        connection = DataConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            addUser = connection.prepareStatement(INSERT_USER);
            getNameList = connection.prepareStatement(GET_USERNAMES);
            getUser = connection.prepareStatement(GET_USER);
            deleteUser = connection.prepareStatement(DELETE_USER);
            editUser = connection.prepareStatement(EDIT_USER);
            rowCount = connection.prepareStatement(COUNT_ROWS);
            isValidUser = connection.prepareStatement(GET_VALID);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see UserSources#addUser(UserInfo)
     */
    public void addUser(UserInfo u) {
        try {
            addUser.setString(1, u.getName());
            addUser.setString(2, u.getUsername());
            addUser.setString(3, u.getPasswords());
            addUser.setString(4, u.getSalt());
            addUser.setString(5, u.getEmail());
            addUser.setString(6, u.getCreateBillboards());
            addUser.setString(7, u.getEditAllBillboards());
            addUser.setString(8, u.getScheduleBillboards());
            addUser.setString(9, u.getEditUsers());
            addUser.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see UserSources#nameSet()
     */
    public Set<String> nameSet() {
        Set<String> usernames = new TreeSet<String>();
        ResultSet rs = null;

        try {
            rs = getNameList.executeQuery();
            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return usernames;
    }

    /**
     * @see UserSources#getUser(String)
     */
    public UserInfo getUser(String username) {
        UserInfo u = new UserInfo();
        ResultSet rs = null;
        try {
            getUser.setString(1, username);
            rs = getUser.executeQuery();
            rs.next();
            u.setName(rs.getString("name"));
            u.setUsername(rs.getString("username"));
            u.setPasswords(rs.getString("passwords"));
            u.setSalt(rs.getString("salt"));
            u.setEmail(rs.getString("email"));
            u.setCreateBillboards(rs.getString("createBillboards"));
            u.setEditAllBillboards(rs.getString("editAllBillboards"));
            u.setEditUsers(rs.getString("editUsers"));
            u.setScheduleBillboards(rs.getString("scheduleBillboards"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return u;
    }

    /**
     * @see
     */
    public int getSize() {
        ResultSet rs = null;
        int rows = 0;

        try {
            rs = rowCount.executeQuery();
            rs.next();
            rows = rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rows;
    }

    /**
     * @see UserSources#deleteUser(String)
     */
    public void deleteUser(String username) {
        try {
            deleteUser.setString(1, username);
            deleteUser.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see UserSources#editUser(String, String, String, String, String, String, String, String, String, String)
     */
    public void editUser(String name, String username, String password, String salt, String email, String previousUsername,
                         String createBillboards, String editAllBillboards,
                         String scheduleBillboards, String editUsers) {
        try {
            editUser.setString(1, name);
            editUser.setString(2, username);
            editUser.setString(3, password);
            editUser.setString(4, salt);
            editUser.setString(5, email);
            editUser.setString(6, createBillboards);
            editUser.setString(7, editAllBillboards);
            editUser.setString(8, scheduleBillboards);
            editUser.setString(9, editUsers);
            editUser.setString(10, previousUsername);
            editUser.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
