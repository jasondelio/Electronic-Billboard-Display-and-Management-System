package cab302.database.schedule;

import cab302.database.DataConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


/**
 * Class for retrieving data SQL query
 */
public class SchedulesDatasource implements ScheduleSources {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS schedules ("
                    + "boardtitle VARCHAR(30),"
                    + "creator VARCHAR(30),"
                    + "year VARCHAR(4),"
                    + "month VARCHAR(3),"
                    + "date VARCHAR(3),"
                    + "hour VARCHAR(3),"
                    + "minute VARCHAR(3),"
                    + "durationHr VARCHAR(3),"
                    + "durationMin VARCHAR(3),"
                    + "recur VARCHAR(3)"
                    + ");";

    private static final String INSERT_SCHEDULE = "INSERT INTO schedules (boardtitle, creator, year, month, date, hour, minute, durationHr, durationMin, recur)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String GET_TITLE = "SELECT boardtitle FROM schedules";

    private static final String TAKE_SCHEDULE = "SELECT boardtitle FROM schedules";

    private static final String GET_CURRENT_BOARD_TITLE = "SELECT * FROM schedules where (year=?) AND (month=?) AND (date=?)";

    private static final String GET_SCHEDULE = "SELECT * FROM schedules WHERE boardtitle=?";

    private static final String GET_SCHEDULE_List = "SELECT * FROM schedules";

    private static final String GET_SAME_SCHEDULE = "SELECT * FROM schedules  WHERE (boardtitle=?) AND (month=?) AND (date=?) AND (hour=?)" +
            "AND (minute=?) AND (durationHr=?) AND (durationMin=?) AND (recur=?)";

    private static final String DELETE_SCHEDULE = "DELETE FROM schedules WHERE (boardtitle=?) AND (month=?) AND (date=?) AND (hour=?) AND (minute=?)";

    private static final String DELETE_ALLSCHEDULE = "DELETE FROM schedules WHERE boardtitle=?";


    private static final String EDIT_SCHEDULE = "UPDATE schedules SET boardtitle=?, creator=?, year=?, month=?, date=?, hour=?, minute=?, durationHr=?" +
            ", durationMin=?, recur=? WHERE (boardtitle=?) AND (month=?) AND (date=?) AND (hour=?)";

    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM schedules";

    private static final String ROW_ITEM = "SELECT * FROM schedules LIMIT ?, 1;";

    private static final String FIND_SCHEDULE = "SELECT * From schedules WHERE (boardtitle=?) AND  (month=?) AND(date=?) AND (hour=?) AND (minute=?)";

    private Connection connection;

    private PreparedStatement createSchedule;

    private PreparedStatement getTitleList;

    private PreparedStatement getCurrentBoardTitle;

    private PreparedStatement getSchedule;

    private PreparedStatement getSameSchedule;

    private PreparedStatement getScheduleList;

    private PreparedStatement deleteSchedule;

    private PreparedStatement deleteAllSchedule;

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
            getCurrentBoardTitle = connection.prepareStatement(GET_CURRENT_BOARD_TITLE);
            getScheduleList = connection.prepareStatement(GET_SCHEDULE_List);
            getSameSchedule = connection.prepareStatement(GET_SAME_SCHEDULE);
            deleteSchedule = connection.prepareStatement(DELETE_SCHEDULE);
            deleteAllSchedule = connection.prepareStatement(DELETE_ALLSCHEDULE);
            editSchedule = connection.prepareStatement(EDIT_SCHEDULE);
            rowCount = connection.prepareStatement(COUNT_ROWS);
            findSchedule = connection.prepareStatement(FIND_SCHEDULE);
            takeSchedule = connection.prepareStatement(TAKE_SCHEDULE);
            findRow = connection. prepareStatement(ROW_ITEM);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Add schedule by the given data of schedule from Custom Dialog
     *
     * @see cab302.ControlPanel.CustomDialog
     */
    public void createSchedule(ScheduleInfo b) {
        try {
            createSchedule.setString(1, b.getBoardTitle());
            createSchedule.setString(2, b.getCreator());
            createSchedule.setString(3, b.getYear());
            createSchedule.setString(4, b.getMonth());
            createSchedule.setString(5, b.getDate());
            createSchedule.setString(6, b.getHour());
            createSchedule.setString(7, b.getMinute());
            createSchedule.setString(8, b.getDuHr());
            createSchedule.setString(9, b.getDuMin());
            createSchedule.setString(10, b.getRecur());
            createSchedule.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create the tree set to store the billboard title
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
     * Find schedule by searching the given title
     *
     * @param title : the title of schedule
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
                b.setYear(rs.getString("year"));
                b.setMonth(rs.getString("month"));
                b.setDate(rs.getString("date"));
                b.setHour(rs.getString("hour"));
                b.setMinute(rs.getString("minute"));
                b.setDuHr(rs.getString("durationHr"));
                b.setDuMin(rs.getString("durationMin"));
                b.setRecur(rs.getString("recur"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /**
     * Find same schedule comparing to a new schedule from the database
     *
     * @param title       : the title of schedule to be found from the current database
     * @param month       : the month of schedule to be found from the current database
     * @param date        : the date of schedule to be found from the current database
     * @param hour        : the hour of schedule to be found from the current database
     * @param minute      : the minute of schedule to be found from the current database
     * @param durationHr  : the duration hours of schedule to be found from the current database
     * @param durationMin : the duration minutes of schedule to be found from the current database
     * @param recur       : the frequency of schedule to be found from the current database
     * @return ScheduleInfo
     */
    public ScheduleInfo findSame(String title, String month, String date, String hour, String minute, String durationHr, String durationMin, String recur) {
        ScheduleInfo b = new ScheduleInfo();
        ResultSet rs = null;
        try {
            getSameSchedule.setString(1, title);
            getSameSchedule.setString(2, month);
            getSameSchedule.setString(3, date);
            getSameSchedule.setString(4, hour);
            getSameSchedule.setString(5, minute);
            getSameSchedule.setString(6, durationHr);
            getSameSchedule.setString(7, durationMin);
            getSameSchedule.setString(8, recur);
            rs = getSameSchedule.executeQuery();
            while (rs.next()) {
                b.setBoardTitle(rs.getString("boardtitle"));
                b.setCreator(rs.getString("creator"));
                b.setYear(rs.getString("year"));
                b.setMonth(rs.getString("month"));
                b.setDate(rs.getString("date"));
                b.setHour(rs.getString("hour"));
                b.setMinute(rs.getString("minute"));
                b.setDuHr(rs.getString("durationHr"));
                b.setDuMin(rs.getString("durationMin"));
                b.setRecur(rs.getString("recur"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /**
     * Find schedule by using the given information that are title, date, and hour
     *
     * @param title : the title of schedule to be found
     * @param month : the month of schedule to be found
     * @param date : the date of schedule to be found
     * @param hour : the hour of schedule to be found
     * @param minute : the minute of schedule to be found
     * @return schedule info
     */
    public ScheduleInfo findSchedule(String title, String month, String date, String hour, String minute) {
        ScheduleInfo b = new ScheduleInfo();
        ResultSet rs = null;
        try {
            findSchedule.setString(1, title);
            findSchedule.setString(2, month);
            findSchedule.setString(3, date);
            findSchedule.setString(4, hour);
            findSchedule.setString(5, minute);
            rs = findSchedule.executeQuery();
            while (rs.next()) {
                b.setBoardTitle(rs.getString("boardtitle"));
                b.setCreator(rs.getString("creator"));
                b.setYear(rs.getString("year"));
                b.setMonth(rs.getString("month"));
                b.setDate(rs.getString("date"));
                b.setHour(rs.getString("hour"));
                b.setMinute(rs.getString("minute"));
                b.setDuHr(rs.getString("durationHr"));
                b.setDuMin(rs.getString("durationMin"));
                b.setRecur(rs.getString("recur"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /**
     * Get the titles of billboard on current time
     *
     * @param year : the year of schedule to get the required title of billboard
     * @param month : the month of schedule to get the required title of billboard
     * @param date : the date of schedule to get the required title of billboard
     * @return array list of title
     */
    public ArrayList<ScheduleInfo> getCurrentBillboardTitle(String year, String month, String date) {

        ArrayList<ScheduleInfo> titles = new ArrayList<ScheduleInfo>();
        ResultSet rs = null;

        try {
            getCurrentBoardTitle.setString(1, year);
            getCurrentBoardTitle.setString(2, month);
            getCurrentBoardTitle.setString(3, date);
            rs = getCurrentBoardTitle.executeQuery();
            while (rs.next()) {
                ScheduleInfo si = new ScheduleInfo();
                si.setBoardTitle(rs.getString("boardtitle"));
                si.setCreator(rs.getString("creator"));
                si.setYear(rs.getString("year"));
                si.setMonth(rs.getString("month"));
                si.setDate(rs.getString("date"));
                si.setHour(rs.getString("hour"));
                si.setMinute(rs.getString("minute"));
                si.setDuHr(rs.getString("durationHr"));
                si.setDuMin(rs.getString("durationMin"));
                si.setRecur(rs.getString("recur"));
                titles.add(si);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return titles;
    }

    /**
     * Find the schedule which is on specific row
     *
     * @param index : the index of the schedule that needs to be found
     * @return schedule info
     */
    public ScheduleInfo findRow(int index) {
        ScheduleInfo b = new ScheduleInfo();
        ResultSet rs = null;
        try {
            findRow.setInt(1, index);
            rs = findRow.executeQuery();
            while (rs.next()) {
                b.setBoardTitle(rs.getString("boardtitle"));
                b.setCreator(rs.getString("creator"));
                b.setYear(rs.getString("year"));
                b.setMonth(rs.getString("month"));
                b.setDate(rs.getString("date"));
                b.setHour(rs.getString("hour"));
                b.setMinute(rs.getString("minute"));
                b.setDuHr(rs.getString("durationHr"));
                b.setDuMin(rs.getString("durationMin"));
                b.setRecur(rs.getString("recur"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /**
     * Take every billboard titles even it is duplicated
     *
     * @return DefaultlistModel n
     */
    public DefaultListModel takeSchedule() {
        DefaultListModel n = new DefaultListModel();
        ArrayList<String> a = new ArrayList<>();

        ResultSet rs = null;
        try {
            rs = takeSchedule.executeQuery();
            while (rs.next()) {
                n.addElement(rs.getString("boardtitle"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }


    /**
     * Get the size of table
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
     * Take the schedule for server
     *
     * @return String Set
     */
    public Set<String> GetScheduleList() {
        Set<String> schedules = new TreeSet<String>();
        ResultSet rs = null;

        try {
            rs = getScheduleList.executeQuery();
            while (rs.next()) {
                schedules.add(rs.getString("boardtitle") + " made by " + rs.getString("creator") + ": Scheduled " + rs.getString("duration")
                        + " hour on " + rs.getInt("month") + "/" + rs.getInt("date") + "/" + rs.getString("hour") + "/"
                        + rs.getString("minute") + "(mm/dd/hh/mm):");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return schedules;
    }

    /**
     * Delete schedule by searching title, month, date and hour
     *
     * @param title : the title of a schedule which has to be removed
     * @param month : the month of a schedule which has to be removed
     * @param date : the date of a schedule which has to be removed
     * @param hour : the hour of a schedule which has to be removed
     * @param minute : the minute of a schedule which has to be removed
     */
    public void deleteSchedule(String title, String month, String date, String hour,String minute) {
        try {
            deleteSchedule.setString(1, title);
            deleteSchedule.setString(2, month);
            deleteSchedule.setString(3, date);
            deleteSchedule.setString(4, hour);
            deleteSchedule.setString(5,minute);
            deleteSchedule.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Delete all schedule with same title from the database
     * @param title : the title of a schedule
     */
    public void deleteAllSchedule(String title) {
        try {
            deleteAllSchedule.setString(1, title);
            deleteAllSchedule.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Edit schedule information
     * @param boardtitle : the title of schedule that must be edited
     * @param creator : the creator of schedule that must be edited
     * @param year : the year of schedule that must be edited
     * @param month : the month of schedule that must be edited
     * @param date : the date of schedule that must be edited
     * @param hour : the hour of schedule that must be edited
     * @param minute : the minute of schedule that must be edited
     * @param duHr : the duration hours of schedule that must be edited
     * @param duMin : the duration minutes of schedule that must be edited
     * @param recur : the frequency of schedule that must be edited
     */
    public void editSchedule(String boardtitle, String creator, String year, String month, String date, String hour,
                             String minute, String duHr, String duMin, String recur) {
        try {
            editSchedule.setString(1, boardtitle);
            editSchedule.setString(2, creator);
            editSchedule.setString(3, year);
            editSchedule.setString(4, month);
            editSchedule.setString(5, date);
            editSchedule.setString(6, hour);
            editSchedule.setString(7, minute);
            editSchedule.setString(8, duHr);
            editSchedule.setString(9, duMin);
            editSchedule.setString(10, recur);
            editSchedule.setString(11, boardtitle);
            editSchedule.setString(12, month);
            editSchedule.setString(13, date);
            editSchedule.setString(14, hour);
            editSchedule.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * close the connection
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
