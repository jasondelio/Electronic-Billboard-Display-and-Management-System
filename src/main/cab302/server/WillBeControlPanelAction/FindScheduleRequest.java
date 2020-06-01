package cab302.server.WillBeControlPanelAction;

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
    public FindScheduleRequest(String sessiontoken, String title, String month, String date, String hour){
        this.sessiontoken =sessiontoken;
        this.title = title;
        this.month = month;
        this.date = date;
        this.hour = hour;
    }
    public String getSessiontoken(){
        return sessiontoken;
    }

    public String getMonth() {
        return month;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }
}
