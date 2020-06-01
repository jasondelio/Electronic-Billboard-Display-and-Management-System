package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

/**
 * Request getting list of billboards to server and the server get sessiontoken from client
 */
public class ListBillboardRequest implements Serializable {
    private String sessionToken;
    public ListBillboardRequest(String sessionToken){
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

