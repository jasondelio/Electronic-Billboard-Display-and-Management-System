package cab302.database.schedule;

import cab302.database.DataConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class SchedulesDatasource implements ScheduleSources {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS schedules ("
                    + "boardtitle VARCHAR(30),"
                    + "creator VARCHAR(30),"
                    + "month VARCHAR(3),"
                    + "date VARCHAR(3),"
                    + "hour VARCHAR(3),"
                    + "minute VARCHAR(3),"
                    + "duration VARCHAR(3)"
                    + ");";

    private static final String INSERT_SCHEDULE = "INSERT INTO schedules (boardtitle, creator, month, date, hour, minute, duration) VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String GET_TITLE = "SELECT boardtitle FROM schedules";

    private static final String TAKE_SCHEDULE = "SELECT boardtitle FROM schedules";

    private static final String GET_SCHEDULE = "SELECT * FROM schedules WHERE boardtitle=?";

    private static final String GET_SCHEDULE_List = "SELECT * FROM schedules";

    private static final String DELETE_SCHEDULE = "DELETE FROM schedules WHERE boardtitle=?";

    private static final String EDIT_SCHEDULE = "UPDATE schedules SET boardtitle=?, creator=?, month=?, date=?, hour=?, minute=?, duration=? WHERE boardtitle=?";

    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM schedules";

    private static final String ROW_ITEM = "SELECT * FROM schedules LIMIT ?, 1;";

    private static final String FIND_SCHEDULE = "SELECT * From schedules WHERE (month=?) AND (date=?)";

    private Connection connection;

    private PreparedStatement createSchedule;

    private PreparedStatement getTitleList;

    private PreparedStatement getSchedule;

    private PreparedStatement getScheduleList;

    private PreparedStatement deleteSchedule;

    private PreparedStatement editSchedule;

    private PreparedStatement rowCount;

    private PreparedStatement findSchedule;

    private PreparedStatement takeSchedule;

    private PreparedStatement findRow;

    public SchedulesDatasource() {
        connection = DataConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            createSchedule = connection.prepareStatement(INSERT_SCHEDULE);
            getTitleList = connection.prepareStatement(GET_TITLE);
            getSchedule = connection.prepareStatement(GET_SCHEDULE);
            getScheduleList = connection.prepareStatement(GET_SCHEDULE_List);
            deleteSchedule = connection.prepareStatement(DELETE_SCHEDULE);
            editSchedule = connection.prepareStatement(EDIT_SCHEDULE);
            rowCount = connection.prepareStatement(COUNT_ROWS);
            findSchedule = connection.prepareStatement(FIND_SCHEDULE);
            takeSchedule = connection.prepareStatement(TAKE_SCHEDULE);
            findRow = connection. prepareStatement(ROW_ITEM);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        System.out.println("Table created");
    }

    /**
     * @see
     */
    public void createSchedule(ScheduleInfo b) {
        try {
            createSchedule.setString(1, b.getBoardTitle());
            createSchedule.setString(2, b.getCreator());
            createSchedule.setString(3, b.getMonth());
            createSchedule.setString(4, b.getDate());
            createSchedule.setString(5, b.getHour());
            createSchedule.setString(6, b.getMinute());
            createSchedule.setString(7, b.getDuration());
            createSchedule.execute();
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
                titles.add(rs.getString("boardtitle"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return titles;
    }

    /**
     * @see
     */
    public ScheduleInfo getSchedule(String title) {
        ScheduleInfo b = new ScheduleInfo();
        ResultSet rs = null;
        try {
            getSchedule.setString(1, title);
            rs = getSchedule.executeQuery();
            while(rs.next()) {
                b.setBoardTitle(rs.getString("boardtitle"));
                b.setCreator(rs.getString("creator"));
                b.setMonth(rs.getString("month"));
                b.setDate(rs.getString("date"));
                b.setHour(rs.getString("hour"));
                b.setMinute(rs.getString("minute"));
                b.setDuration(rs.getString("duration"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b;
    }

    public ScheduleInfo findSchedule(String month, String date) {
        ScheduleInfo b = new ScheduleInfo();
        ResultSet rs = null;
        try {
            findSchedule.setString(1, month);
            findSchedule.setString(2, date);
            rs = findSchedule.executeQuery();
            while(rs.next()) {
                b.setBoardTitle(rs.getString("boardtitle"));
                b.setHour(rs.getString("hour"));
                b.setMinute(rs.getString("minute"));
                b.setDuration(rs.getString("duration"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b;
    }

    public ScheduleInfo findRow(int index) {
        ScheduleInfo b = new ScheduleInfo();
        ResultSet rs = null;
        try {
            findRow.setInt(1, index);
            rs = findRow.executeQuery();
            while(rs.next()) {
                b.setBoardTitle(rs.getString("boardtitle"));
                b.setCreator(rs.getString("creator"));
                b.setMonth(rs.getString("month"));
                b.setDate(rs.getString("date"));
                b.setHour(rs.getString("hour"));
                b.setMinute(rs.getString("minute"));
                b.setDuration(rs.getString("duration"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b;
    }

    public DefaultListModel takeSchedule(){
        DefaultListModel n = new DefaultListModel();
        ArrayList<String> a = new ArrayList<>();

        ResultSet rs = null;
        try {
            rs = takeSchedule.executeQuery();
            while (rs.next()) {
                n.addElement(rs.getString("boardtitle"));
//                a.add(rs.getString("boardtitle") + ", " +
//                        rs.getString("month") + ", " +
//                        rs.getString("date") + ", " +
//                        rs.getString("hour") + ", " +
//                        rs.getString("minute") + ", " +
//                        rs.getString("duration") + "/");
//                n.addElement(a);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
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
    // im using this in server
    public Set<String> GetScheduleList() {
        Set<String> schedules = new TreeSet<String>();
        ResultSet rs = null;

        try {
            rs = getScheduleList.executeQuery();
            while (rs.next()) {
                schedules.add(rs.getString("boardtitle")+" made by "+rs.getString("creator")+": Scheduled "+ rs.getString("duration")
                        +" hour on "+rs.getInt("month")+"/"+rs.getInt("date")+"/"+rs.getString("hour")+"/"
                        +rs.getString("minute") + "(mm/dd/hh/mm):");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return schedules;
    }

    /**
     * @see
     */
    public void deleteSchedule(String title) {
        try {
            deleteSchedule.setString(1, title);
            deleteSchedule.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @see
     * @return
     */
    public void editSchedule(String boardtitle, String creator, String month, String date, String hour,
                                     String minute, String duration) {
        try {
            editSchedule.setString(1, boardtitle);
            editSchedule.setString(2, creator);
            editSchedule.setString(3, month);
            editSchedule.setString(4, date);
            editSchedule.setString(5, hour);
            editSchedule.setString(6, minute);
            editSchedule.setString(7, duration);
            editSchedule.setString(8, boardtitle);
            editSchedule.execute();
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
