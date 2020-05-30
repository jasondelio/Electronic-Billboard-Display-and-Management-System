package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class EditScheduleBillboard implements Serializable {
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


    public EditScheduleBillboard(String sessionToken, String boardtitle, String creator, String year, String month, String date, String hour,
                                 String minute, String duHr, String duMin, String recur) {
        this.sessionToken = sessionToken;
        this.billboardname = boardtitle;
        this.creator = creator;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minitue = minute;
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

    public String getMinitue() {
        return minitue;
    }

    public String getYear() {
        return year;
    }
}

