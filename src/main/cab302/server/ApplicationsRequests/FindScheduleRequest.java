package cab302.server.ApplicationsRequests;

import java.io.Serializable;
/**
 * Request finding schedule to server and the server get sessiontoken, creator, time, duration time and billnboard name from client
 */
public class FindScheduleRequest  implements Serializable {
    private String sessiontoken;
    private String title;
    private String month;
    private String date;
    private String hour;
    private String minute;

    /**
     *  Constructor setting values to find schedule with below information in server
     * @param title billboard's title
     * @param sessiontoken sessionToken
     * @param month month
     * @param date date
     * @param hour hour
     * @param minute minute
     */
    public FindScheduleRequest(String sessiontoken, String title, String month, String date, String hour, String minute){
        this.sessiontoken =sessiontoken;
        this.title = title;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * @return sessionToken
     */
    public String getSessiontoken(){
        return sessiontoken;
    }

    /**
     * @return month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
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
}
