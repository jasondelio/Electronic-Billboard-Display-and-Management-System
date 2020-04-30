/**
 * The Code for basic of our billboard source.
 * Unsure it will be worked well or not.
 */
package cab302.database.user;

import java.sql.*;
import java.util.*;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class UserDataSource implements UserSources {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS userinfo ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "name VARCHAR(30),"
                    + "id VARCHAR(30),"
                    + "passwords VARCHAR(20),"
                    + "email VARCHAR(30)" + ");";

    private static final String INSERT_USER = "INSERT INTO userinfo (name, id, passwords, email) VALUES (?, ?, ?, ?);";

    private static final String GET_NAMES = "SELECT name FROM userinfo";

    private static final String GET_USER = "SELECT * FROM userinfo WHERE name=?";

    private static final String DELETE_USER = "DELETE FROM userinfo WHERE name=?";

    private static final String EDIT_USER = "UPDATE FROM userinfo SET name=?, passwords=?, email=? WHERE id=?";

    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM address";

    private Connection connection;

    private PreparedStatement addUser;

    private PreparedStatement getNameList;

    private PreparedStatement getUser;

    private PreparedStatement deleteUser;

    private PreparedStatement editUser;

    private PreparedStatement rowCount;

    public UserDataSource() {
        connection = DataConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            addUser = connection.prepareStatement(INSERT_USER);
            getNameList = connection.prepareStatement(GET_NAMES);
            getUser = connection.prepareStatement(GET_USER);
            deleteUser = connection.prepareStatement(DELETE_USER);
            editUser = connection.prepareStatement(EDIT_USER);
            rowCount = connection.prepareStatement(COUNT_ROWS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
     */
    public void addUser(UserInfo u) {
        try {
            addUser.setString(1, u.getName());
            addUser.setString(2, u.getId());
            addUser.setString(3, u.getPasswords());
            addUser.setString(5, u.getEmail());
            addUser.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
     */
    public Set<String> nameSet() {
        Set<String> names = new TreeSet<String>();
        ResultSet rs = null;

        try {
            rs = getNameList.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return names;
    }

    /**
     * @see
     */
    public UserInfo getUser(String name) {
        UserInfo u = new UserInfo();
        ResultSet rs = null;
        try {
            getUser.setString(1, name);
            rs = getUser.executeQuery();
            rs.next();
            u.setName(rs.getString("name"));
            u.setId(rs.getString("id"));
            u.setPasswords(rs.getString("passwords"));
            u.setEmail(rs.getString("email"));
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
     * @see
     */
    public void deleteUser(String name) {
        try {
            deleteUser.setString(1, name);
            deleteUser.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
     * @return
     */
    public UserInfo editUser(String name) {
        UserInfo n = new UserInfo();
        ResultSet r = null;
        try {
            editUser.setString(1, name);
            r = editUser.executeQuery();
            r.next();
            n.setName(r.getString("name"));
            n.setPasswords(r.getString("passwords"));
            n.setEmail(r.getString("email"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
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
