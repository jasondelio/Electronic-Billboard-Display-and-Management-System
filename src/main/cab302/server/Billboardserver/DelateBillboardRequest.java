package cab302.server.Billboardserver;

import java.io.Serializable;

public class DelateBillboardRequest implements Serializable {
    private String billboardname;
    private String sessionToken;
    public DelateBillboardRequest(String billboardname, String sessionToken) {
        this.billboardname = billboardname;
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getBillboardname() {
        return billboardname;
    }
}

