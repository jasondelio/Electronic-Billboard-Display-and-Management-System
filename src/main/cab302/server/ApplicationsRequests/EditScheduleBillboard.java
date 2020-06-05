package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request editing schedule to server and the server get sessiontoken, creator, time, duration time and billnboard name from client
 */
public class EditScheduleBillboard implements Serializable {
    private String billboardname;
    private String creator;
    private String year;
    private String month;
    private String date;
    private String hour;
    private String minute;
    private String new_minute;
    private String sessionToken;
    private String durationHr;
    private String durationMin;
    private String recur;

    /**
     *  Constructor setting values to edit schedule in server
     * @param boardtitle billboard's title
     * @param sessionToken sessiontoken
     * @param creator billboard's crator
     * @param year year
     * @param month month
     * @param date date
     * @param hour hour
     * @param minute minute
     * @param new_minute new_minute will be changed
     * @param duHr duration hour
     * @param duMin duration minute
     * @param recur recurring time
     */
    public EditScheduleBillboard(String sessionToken, String boardtitle, String creator, String year, String month, String date, String hour,
                                 String minute, String new_minute, String duHr, String duMin, String recur) {
        this.sessionToken = sessionToken;
        this.billboardname = boardtitle;
        this.creator = creator;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.new_minute = new_minute;
        this.durationHr = duHr;
        this.durationMin = duMin;
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
    public String getDurationHr() {
        return durationHr;
    }

    /**
     * @return durationMin
     */
    public String getDurationMin() {
        return durationMin;
    }

    /**
     * @return recur
     */
    public String getRecur() {
        return recur;
    }

    /**
     * @return month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return hour
     */
    public String getHour() {
        return hour;
    }

    /**
     * @return minute
     */
    public String getMinute() {
        return minute;
    }

    /**
     * @return new_minute
     */
    public String getNew_minute() { return new_minute; }

    /**
     * @return year
     */
    public String getYear() {
        return year;
    }
}

