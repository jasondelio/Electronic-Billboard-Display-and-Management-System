package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;
/**
 * Request getting user's permissions to server and the server get sessiontoken and username from client
 */
public class GetUserPemmRequest implements Serializable {
    private String username;
    private String sessionToken;
    public GetUserPemmRequest (String username, String sessionToken){
        this.sessionToken = sessionToken;
        this.username = username;
    }

    public String getSessionToken() {
        return sessionToken;
    }
    public String getUsername(){
        return username;
    }
}

