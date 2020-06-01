package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

/**
 * Request logout user to server and the server get sessiontoken from client
 */
public class LogoutUsersRequest implements Serializable {
    private String sessionToken;

    public LogoutUsersRequest(String sessiontoken){

        this.sessionToken = sessiontoken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

}

