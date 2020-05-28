package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class ListBillboardRequest implements Serializable {
    private String sessionToken;

    public ListBillboardRequest(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

