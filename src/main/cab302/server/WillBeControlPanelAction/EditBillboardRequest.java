package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;


/**
 * Request editing billboard to server and the server get sessiontoken, billboard Content and billnboard name from client
 */
public class EditBillboardRequest implements Serializable {
    private String billboardname;
    private String billboardContent;
    private String sessionToken;
    public EditBillboardRequest(String billboardname, String billboardContent , String sessionToken) {
        this.billboardname = billboardname;
        this.billboardContent = billboardContent;
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getBillboardContent() { return billboardContent; }

    public String getBillboardname() {
        return billboardname;
    }
}

