package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

/**
 * Request viewing billboard to server and the server get sessiontoken from client
 */
public class ViewBillboardRequest implements Serializable {
    private String sessionToken;

    /**
     * Constructor setting value to view billboard
     * @param sessionToken sessionToken
     */
    public ViewBillboardRequest(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

}

