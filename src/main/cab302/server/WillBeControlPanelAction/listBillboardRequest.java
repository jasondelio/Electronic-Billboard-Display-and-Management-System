package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class listBillboardRequest implements Serializable {
    private String sessionToken;
    public listBillboardRequest(String sessionToken){
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

