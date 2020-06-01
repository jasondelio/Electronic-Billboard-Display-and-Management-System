package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

/**
 * Request confirming if the sessiontoken is not expired yet to server and the server get sessiontoken from client
 */
public class sessionExistRequest implements Serializable {
    private String sessionToken;
    public sessionExistRequest(String sessionToken){
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

