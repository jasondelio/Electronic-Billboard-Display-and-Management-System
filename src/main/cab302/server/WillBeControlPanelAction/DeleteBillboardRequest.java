package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class DeleteBillboardRequest implements Serializable {
    private String billboardname;
    private String sessionToken;

    public DeleteBillboardRequest(String billboardname, String sessionToken) {
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

