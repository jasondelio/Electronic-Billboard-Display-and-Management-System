package cab302.server.ApplicationsRequests;

import java.io.Serializable;
/**
 * Request scheduling billboard to server and the server get sessiontoken,time, duration time, creator, recur and billboard title from client
 */
public class RecurScheduleBillboardRequest implements Serializable {
    private String billboardname;
    private String creator;
    private String year;
    private String month;
    private String date;
    private String hour;
    private String minute;
    private String sessionToken;
    private String durationHr;
    private String durationMin;
    private String recur;

    /**
     *  Constructor setting values to schedule billboard with recur in server
     * @param billboardname billboard's title
     * @param sessionToken sessionToken
     * @param creator billboard's creator
     * @param year year
     * @param month month
     * @param date date
     * @param hour hour
     * @param minute minute
     * @param durationHr duration hour
     * @param durationMin duration minute
     * @param recur recurring time
     */
    public RecurScheduleBillboardRequest(String billboardname, String creator, String year, String month, String date, String hour, String minute, String sessionToken, String durationHr, String durationMin, String recur) {
        this.billboardname = billboardname;
        this.creator = creator;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.sessionToken = sessionToken;
        this.durationHr = durationHr;
        this.durationMin = durationMin;
        this.recur = recur;
    }

    /**
     * @return sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * @return creator
     */
    public String getCreator(){return creator; }

    /**
     * @return billboardname
     */
    public String getBillboardname() {
        return billboardname;
    }

    /**
     * @return durationHr
     */
    public String getDurationHr() { return durationHr; }

    /**
     * @return durationMin
     */
    public String getDurationMin() { return durationMin; }

    /**
     * @return recur
     */
    public String getRecur() { return recur; }

    /**
     * @return month
     */
    public String getMonth() { return month; }

    /**
     * @return date
     */
    public String getDate() { return date; }

    /**
     * @return hour
     */
    public String getHour() { return hour; }

    /**
     * @return minute
     */
    public String getminute() { return minute; }

    /**
     * @return year
     */
    public String getYear() { return year; }
}

