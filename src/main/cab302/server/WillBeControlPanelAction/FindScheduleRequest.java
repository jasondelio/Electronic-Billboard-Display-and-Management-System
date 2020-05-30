package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class FindScheduleRequest  implements Serializable {
    private String sessiontoken;
    private String title;
    private String date;
    private String hour;
    public FindScheduleRequest(String sessiontoken, String title, String date, String hour){
        this.sessiontoken =sessiontoken;
        this.title = title;
        this.date = date;
        this.hour = hour;
    }
    public String getSessiontoken(){
        return sessiontoken;
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
