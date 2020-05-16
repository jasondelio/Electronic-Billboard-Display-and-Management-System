package cab302.server.Billboardserver;

import java.io.Serializable;

public class RemoveBillboardRequest implements Serializable {
    private String billboardname;
    private int time;
    private String sessionToken;
    public RemoveBillboardRequest(String billboardname, String sessionToken, int time) {
        this.billboardname = billboardname;
        this.sessionToken = sessionToken;
        this.time = time;

    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getBillboardname() {
        return billboardname;
    }

    public int getTime() {
        return time;
    }
}


