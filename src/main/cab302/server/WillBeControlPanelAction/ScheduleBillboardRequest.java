package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class ScheduleBillboardRequest implements Serializable {
    private String billboardname;
    private int month;
    private int date;
    private String hour;
    private String minitue;
    private String duration;
    private String sessionToken;
    public ScheduleBillboardRequest(String billboardname, int month, int date, String hour, String minitue, String duration, String sessionToken) {
        this.billboardname = billboardname;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minitue = minitue;
        this.duration = duration;
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getBillboardname() {
        return billboardname;
    }

    public String getDuration() {
        return duration;
    }

    public int getMonth() {
        return month;
    }
    public int getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }
    public String getMinitue() {
        return minitue;
    }

}

