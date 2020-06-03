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
     * @param duHr duration hour
     * @param duMin duration minute
     * @param recur recurring time
     */
    public EditScheduleBillboard(String sessionToken, String boardtitle, String creator, String year, String month, String date, String hour,
                                 String minute, String duHr, String duMin, String recur) {
        this.sessionToken = sessionToken;
        this.billboardname = boardtitle;
        this.creator = creator;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.durationHr = duHr;
        this.durationMin = duMin;
        this.recur = recur;
    }

    public String getSessionToken() {
        return sessionToken;
    }
    public String getCreator(){return creator; }
    public String getBillboardname() {
        return billboardname;
    }

    public String getDurationHr() {
        return durationHr;
    }
    public String getDurationMin() {
        return durationMin;
    }
    public String getRecur() {
        return recur;
    }

    public String getMonth() {
        return month;
    }
    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getYear() {
        return year;
    }
}

