package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

/**
 * Request logout user to server and the server get sessiontoken from client
 */
public class LogoutUsersRequest implements Serializable {
    private String sessionToken;

    /**
     * Constructor setting values to log out in server
     * @param sessiontoken sessionToken
     */
    public LogoutUsersRequest(String sessiontoken){

        this.sessionToken = sessiontoken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

}

