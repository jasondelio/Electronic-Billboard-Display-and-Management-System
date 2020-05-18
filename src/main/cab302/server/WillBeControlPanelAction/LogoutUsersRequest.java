package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class LogoutUsersRequest implements Serializable {
    private String sessionToken;

    public LogoutUsersRequest(String sessiontoken){

        this.sessionToken = sessiontoken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

}

