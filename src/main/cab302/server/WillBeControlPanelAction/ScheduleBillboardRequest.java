package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

/**
 * Request scheduling billboard to server and the server get sessiontoken,time, duration time, creator, recur and billboard title from client
 */
public class ScheduleBillboardRequest implements Serializable {
    private String billboardname;
    private String creator;
    private String year;
    private String month;
    private String date;
    private String hour;
    private String minitue;
    private String sessionToken;
    private String durationHr;
    private String durationMin;
    private String recur;


    public ScheduleBillboardRequest(String billboardname,String creator, String year, String month, String date, String hour, String minitue, String sessionToken, String durationHr, String durationMin, String recur) {
        this.billboardname = billboardname;
        this.creator = creator;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minitue = minitue;
        this.sessionToken = sessionToken;
        this.durationHr = durationHr;
        this.durationMin = durationMin;
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

    public String getMinitue() {
        return minitue;
    }

    public String getYear() {
        return year;
    }
}

