package cab302.server.Billboardserver;

import java.io.Serializable;

public class ViewBillboardRequest implements Serializable {
    private String sessionToken;

    public ViewBillboardRequest(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

