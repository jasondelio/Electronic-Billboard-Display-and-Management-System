package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

/**
 * Request getting user's list to server and the server get sessiontoken from client
 */
public class listUsersRequest implements Serializable {
    private String sessionToken;

    /**
     * Constructor setting value to list user in server
     * @param sessionToken sessionToken
     */
    public listUsersRequest(String sessionToken){
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

