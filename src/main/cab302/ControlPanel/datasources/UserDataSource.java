package cab302.controlpanel.datasources;

import cab302.controlpanel.DataConnection;
import cab302.controlpanel.dataobjects.User;
import cab302.controlpanel.sources.UserSources;

import java.sql.*;
import java.util.*;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class UserDataSource implements UserSources {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS users ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "name VARCHAR(30),"
                    + "username VARCHAR(30) UNIQUE,"
                    + "passwords VARCHAR(20),"
                    + "email VARCHAR(30)" + ");";
    public static final String INSERT_ADMINISTRATOR_USER =
            "INSERT IGNORE INTO users(name, username, passwords, email) VALUES ('admin', 'root', 'password', 'root@gmail.com');";

    private static final String INSERT_USER = "INSERT INTO users (name, username, passwords, email) VALUES (?, ?, ?, ?);";

    private static final String GET_NAMES = "SELECT name FROM users";

    private static final String GET_USERNAMES = "SELECT username FROM users";

    private static final String GET_USER = "SELECT * FROM users WHERE username=?";

    private static final String DELETE_USER = "DELETE FROM users WHERE username=?";

    private static final String EDIT_USER = "UPDATE users SET name=?, username=?, passwords=?, email=? WHERE username=?";

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
            st.execute(INSERT_ADMINISTRATOR_USER);
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
     * @see
     */
    public void addUser(User u) {
        try {
            addUser.setString(1, u.getName());
            addUser.setString(2, u.getUsername());
            addUser.setString(3, u.getPasswords());
            addUser.setString(4, u.getEmail());
            addUser.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
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
     * @see
     */
    public User getUser(String username) {
        User u = new User();
        ResultSet rs = null;
        try {
            getUser.setString(1, username);
            rs = getUser.executeQuery();
            rs.next();
            u.setName(rs.getString("name"));
            u.setUsername(rs.getString("username"));
            u.setPasswords(rs.getString("passwords"));
            u.setEmail(rs.getString("email"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return u;
    }

    public boolean isValidUser(String username, String password){
        ResultSet rs = null;
        try {
            isValidUser.setString(1, username);
            isValidUser.setString(2, password);
            rs = isValidUser.executeQuery();
            rs.next();
            if(rs.getString("username") == null)
            {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        return true;
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
     * @see
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
     * @see
     * @return
     */
    public void editUser(String name, String username, String password, String email, String previousUsername) {
        try {
            editUser.setString(1, name);
            editUser.setString(2, username);
            editUser.setString(3, password);
            editUser.setString(4, email);
            editUser.setString(5, previousUsername);
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