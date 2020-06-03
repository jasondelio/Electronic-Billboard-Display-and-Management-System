package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;
/**
 * Requesting to server to get the billboard info and the server get sessiontoken and billboard name from client
 */
public class GetBillboardInfoRequest implements Serializable {
    private String billboardname;
    private String sessionToken;

    /**
     * Constructor setting values to get billboard content from server
     * @param billboardname billboard's title
     * @param sessionToken sessionToken
     */
    public GetBillboardInfoRequest(String billboardname, String sessionToken) {
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

