package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class RemoveBillboardRequest implements Serializable {
    private String billboardname;
    private String month;
    private String date;
    private String hour;
    private String minitue;
    private String sessionToken;

    public RemoveBillboardRequest(String billboardname, String sessionToken, String month, String date, String hour, String minitue) {
        this.billboardname = billboardname;
        this.sessionToken = sessionToken;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minitue = minitue;

    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getBillboardname() {
        return billboardname;
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
}


