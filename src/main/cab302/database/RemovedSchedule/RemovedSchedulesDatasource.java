package cab302.database.RemovedSchedule;

import cab302.database.DataConnection;
import cab302.server.BillboardServer;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;


/**
 * Class for retrieving data from the XML file holding the address list.
 */
public class RemovedSchedulesDatasource implements RemovedScheduleSources {

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS removedschedules ("
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

    private static final String INSERT_SCHEDULE = "INSERT INTO removedschedules (boardtitle, creator, year, month, date, hour, minute, durationHr, durationMin, recur)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_SCHEDULEs = "DELETE FROM removedschedules WHERE boardtitle=?";
    private static final String GET_TITLE = "SELECT boardtitle FROM schedules";
    private static final String GET_SCHEDULE = "SELECT * FROM removedschedules WHERE (boardtitle=?) AND (year=?) AND (month=?) AND (date=?) AND (hour=?) AND" +
            "(minute=?) AND (durationHr=?) AND (durationMin=?)";

    private Connection connection;

    private PreparedStatement getTitleList;
    private PreparedStatement createSchedule;

    private PreparedStatement getSchedule;
    private PreparedStatement delateSchedules;
    /**
     * Add removed schedule by the given data of schedule from server
     * @see BillboardServer
     */
    public RemovedSchedulesDatasource() {
        connection = DataConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            createSchedule = connection.prepareStatement(INSERT_SCHEDULE);
            getTitleList = connection.prepareStatement(GET_TITLE);
            delateSchedules = connection.prepareStatement(DELETE_SCHEDULEs);
            getSchedule = connection.prepareStatement(GET_SCHEDULE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * delete schedule info
     * @param title delete schedule from removed history
     */
    public void delateSchedules(String title) {
        try {
            delateSchedules.setString(1, title);
            delateSchedules.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * create schedule
     * @param b add b in database
     */
    public void createSchedule(RemovedScheduleInfo b) {
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
     * get removed schedule info
     * @param title billboard title
     * @param year year
     * @param month month
     * @param date data
     * @param hour hour
     * @param minute minute
     * @param durationHr duration Hour
     * @param durationMin duration Minute
     */
    public RemovedScheduleInfo getSchedule(String title, String year, String month, String date,String hour, String minute, String durationHr, String durationMin) {
        RemovedScheduleInfo b = new RemovedScheduleInfo();
        ResultSet rs = null;
        try {
            getSchedule.setString(1, title);
            getSchedule.setString(2, year);
            getSchedule.setString(3, month);
            getSchedule.setString(4, date);
            getSchedule.setString(5, hour);
            getSchedule.setString(6, minute);
            getSchedule.setString(7, durationHr);
            getSchedule.setString(8, durationMin);
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
     * title set
     * @return title
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
     * close
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
