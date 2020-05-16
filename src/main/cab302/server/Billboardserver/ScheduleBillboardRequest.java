package cab302.server.Billboardserver;

import java.io.Serializable;

public class ScheduleBillboardRequest implements Serializable {
    private String billboardname;
    private int time;
    private int duration;
    private String sessionToken;
    public ScheduleBillboardRequest(String billboardname, String sessionToken, int time, int duration) {
        this.billboardname = billboardname;
        this.sessionToken = sessionToken;
        this.time = time;
        this.duration = duration;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getBillboardname() {
        return billboardname;
    }

    public int getDuration() {
        return duration;
    }

    public int getTime() {
        return time;
    }
}

