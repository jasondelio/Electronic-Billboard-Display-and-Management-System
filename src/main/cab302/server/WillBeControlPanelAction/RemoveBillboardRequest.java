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
    /**
     *  Constructor setting values to remove schedule from schedules in server
     * @param billboardname billboard's title
     * @param sessionToken sessionToken
     * @param month month
     * @param date date
     * @param hour hour
     * @param minute minute
     */
    public RemoveBillboardRequest(String billboardname, String sessionToken, String month, String date, String hour, String minute) {
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


