package cab302.database.billboard;

import cab302.database.DataConnection;

import java.sql.*;
import java.util.*;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class BillboardsDatasource implements BillboardSources {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS billboards ("
                    + "title VARCHAR(30),"
                    + "singer VARCHAR(30),"
                    + "rank VARCHAR(3)" + ");";

    private static final String INSERT_BILLBOARD = "INSERT INTO billboards (title, signer, rank) VALUES (?, ?, ?);";

    private static final String GET_TITLE = "SELECT title FROM billboards";

    private static final String GET_BILLBOARD = "SELECT * FROM billboards WHERE title=?";

    private static final String DELETE_BILLBOARD = "DELETE FROM billboards WHERE title=?";

    private static final String EDIT_BILLBOARD = "UPDATE FROM billboards SET title=?, signer=?, rank=? WHERE title=?";

    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM billboards";

    private Connection connection;

    private PreparedStatement createBillboard;

    private PreparedStatement getTitleList;

    private PreparedStatement getBillboard;

    private PreparedStatement deleteBillboard;

    private PreparedStatement editBillboard;

    private PreparedStatement rowCount;

    public BillboardsDatasource() {
        connection = DataConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            createBillboard = connection.prepareStatement(INSERT_BILLBOARD);
            getTitleList = connection.prepareStatement(GET_TITLE);
            getBillboard = connection.prepareStatement(GET_BILLBOARD);
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
    public void createBillboard(BillboardInfo b) {
        try {
            createBillboard.setString(1, b.getTitle());
            createBillboard.setString(2, b.getSinger());
            createBillboard.setString(3, b.getRank());
            createBillboard.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
     */
    public Set<String> titleSet() {
        Set<String> titles = new TreeSet<String>();
        ResultSet rs = null;

        try {
            rs = getTitleList.executeQuery();
            while (rs.next()) {
                titles.add(rs.getString("title"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return titles;
    }

    /**
     * @see
     */
    public BillboardInfo getBillboard(String title) {
        BillboardInfo b = new BillboardInfo();
        ResultSet rs = null;
        try {
            getBillboard.setString(1, title);
            rs = getBillboard.executeQuery();
            rs.next();
            b.setTitle(rs.getString("title"));
            b.setSinger(rs.getString("singer"));
            b.setRank(rs.getString("rank"));
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
    public void deleteBillboard(String title) {
        try {
            deleteBillboard.setString(1, title);
            deleteBillboard.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
     * @return
     */
    public BillboardInfo editBillboard(String title) {
        BillboardInfo n = new BillboardInfo();
        ResultSet r = null;
        try {
            editBillboard.setString(1, title);
            r = editBillboard.executeQuery();
            r.next();
            n.setTitle(r.getString("title"));
            n.setSinger(r.getString("singer"));
            n.setRank(r.getString("rank"));
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
