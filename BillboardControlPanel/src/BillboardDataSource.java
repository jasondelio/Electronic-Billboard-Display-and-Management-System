import java.sql.*;
import java.util.*;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class BillboardDataSource implements BillboardSources {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS billboards ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "name VARCHAR(30),"
                    + "xmlContent TEXT" + ");";

    private static final String INSERT_BILLBOARD = "INSERT INTO billboards (name, xmlContent) VALUES (?, ?);";

    private static final String GET_XML = "SELECT xmlContent FROM billboards WHERE name=?";

    private static final String GET_BILLBOARD = "SELECT * FROM billboards WHERE name=?";

    private static final String GET_NAMES = "SELECT name FROM billboards";

    private static final String DELETE_BILLBOARD = "DELETE FROM billboards WHERE name=?";

    private static final String EDIT_BILLBOARD = "UPDATE billboards SET name=?, xmlContent=? WHERE name=?";

    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM billboards";

    private Connection connection;

    private PreparedStatement addBillboard;

    private PreparedStatement getXML;

    private PreparedStatement getBillboard;

    private PreparedStatement getNameList;

    private PreparedStatement deleteBillboard;

    private PreparedStatement editBillboard;

    private PreparedStatement rowCount;


    public BillboardDataSource() {
        connection = DataConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            addBillboard = connection.prepareStatement(INSERT_BILLBOARD);
            getXML = connection.prepareStatement(GET_XML);
            getBillboard = connection.prepareStatement(GET_BILLBOARD);
            getNameList = connection.prepareStatement(GET_NAMES);
            deleteBillboard = connection.prepareStatement(DELETE_BILLBOARD);
            editBillboard = connection.prepareStatement(EDIT_BILLBOARD);
            rowCount = connection.prepareStatement(COUNT_ROWS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
     */
    public void addBillboard(Billboard b) {
        try {
            addBillboard.setString(1, b.getName());
            addBillboard.setString(2, b.getXMLContent());
            addBillboard.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
     */
    public Set<String> nameSet() {
        Set<String> billboardNames = new TreeSet<String>();
        ResultSet rs = null;

        try {
            rs = getNameList.executeQuery();
            while (rs.next()) {
                billboardNames.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return billboardNames;
    }

    /**
     * @see
     */
    public Billboard getBillboard(String name) {
        Billboard b = new Billboard();
        ResultSet rs = null;
        try {
            getBillboard.setString(1, name);
            rs = getBillboard.executeQuery();
            rs.next();
            b.setName(rs.getString("name"));
            b.setXMLContent(rs.getString("xmlContent"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b;
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
    public void deleteBillboard(String name) {
        try {
            deleteBillboard.setString(1, name);
            deleteBillboard.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void editBillboard(String name, String XMLContent, String previousName) {
        try {
            editBillboard.setString(1, name);
            editBillboard.setString(2, XMLContent);
            editBillboard.setString(3, previousName);
            editBillboard.execute();
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