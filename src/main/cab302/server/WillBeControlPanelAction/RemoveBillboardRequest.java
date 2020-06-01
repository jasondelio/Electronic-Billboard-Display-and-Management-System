package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

/**
 * Request removing billboard to server and the server get sessiontoken, month, date, hour, minute and billboard title from client
 */
public class RemoveBillboardRequest implements Serializable {
    private String billboardname;
    private String month;
    private String date;
    private String hour;
    private String minute;
    private String sessionToken;

    public RemoveBillboardRequest(String billboardname, String sessionToken, String month, String date, String hour) {
        this.billboardname = billboardname;
        this.sessionToken = sessionToken;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;

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

    public String getMinute() {
        return minute;
    }
}


