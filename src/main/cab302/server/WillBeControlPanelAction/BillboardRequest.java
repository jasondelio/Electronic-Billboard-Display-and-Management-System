package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;
/**
 * Requesting to server to get the billboard info and the server get sessiontoken and billboard name from client
 */
public class BillboardRequest implements Serializable {
    private String billboardname;
    private String sessionToken;
    public BillboardRequest(String billboardname, String sessionToken) {
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

