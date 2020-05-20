package cab302.database.schedule;

import cab302.database.DataConnection;


import javax.swing.*;
import java.sql.*;
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

    private static final String INSERT_SCHEDULE = "INSERT INTO schedules (boradtitle, creator, month, date, hour, minute, duration) VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String GET_TITLE = "SELECT boardtitle FROM schedules";

    private static final String TAKE_SCHEDULE = "SELECT boardtitle, month, date, hour, minute, duration FROM schedules";

    private static final String GET_SCHEDULE = "SELECT * FROM schedules WHERE boardtitle=?";

    private static final String GET_SCHEDULE_List = "SELECT * FROM schedules";

    private static final String DELETE_SCHEDULE = "DELETE FROM schedules WHERE boardtitle=?";

    private static final String EDIT_SCHEDULE = "UPDATE FROM schedules SET month=?, hour=?, minute=?, duration=? WHERE boardtitle=?";

    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM schedules";

    private static final String FIND_SCHEDULE = "SELECT boardtitle From schedules WHERE (month=?) AND (date=?)";

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
            createSchedule.setString(4, b.getHour());
            createSchedule.setString(5, b.getMinute());
            createSchedule.setString(6, b.getDuration());
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
            rs.next();
            b.setBoardTitle(rs.getString("boardtitle"));
            b.setCreator(rs.getString("creator"));
            b.setMonth(rs.getString("month"));
            b.setDate(rs.getString("date"));
            b.setHour(rs.getString("hour"));
            b.setMinute(rs.getString("minute"));
            b.setDuration(rs.getString("duration"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b;
    }

    public DefaultListModel takeSchedule(){
        DefaultListModel n = new DefaultListModel();
        ResultSet rs = null;
        try {
            rs = takeSchedule.executeQuery();
            while (rs.next()) {
                n.addElement(rs.getString("boardtitle"));
                n.addElement(rs.getString("month"));
                n.addElement(rs.getString("date"));
                n.addElement(rs.getString("hour"));
                n.addElement(rs.getString("minute"));
                n.addElement(rs.getString("duration"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public ScheduleInfo findSchedule(String date, String month){
        ScheduleInfo n = new ScheduleInfo();
        ResultSet rs = null;
        try {
            findSchedule.setString(1, date);
            findSchedule.setString(2, month);
            rs = getSchedule.executeQuery();
            rs.next();
            n.setBoardTitle(rs.getString("boardtitle"));
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
    public ScheduleInfo editSchedule(String title) {
        ScheduleInfo n = new ScheduleInfo();
        ResultSet r = null;
        try {
            editSchedule.setString(1, title);
            r = editSchedule.executeQuery();
            r.next();
            n.setBoardTitle(r.getString("boardtitle"));
            n.setCreator(r.getString("creator"));
            n.setMonth(r.getString("month"));
            n.setDate(r.getString("date"));
            n.setHour(r.getString("hour"));
            n.setMinute(r.getString("minute"));
            n.setDuration(r.getString("duration"));
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
