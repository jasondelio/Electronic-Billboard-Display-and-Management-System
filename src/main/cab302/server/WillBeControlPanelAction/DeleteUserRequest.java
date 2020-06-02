package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

/**
 * Request delating user to server and the server get sessiontoken and username from client
 */
public class DeleteUserRequest implements Serializable {
    private String username;
    private String sessiontoken;

    public DeleteUserRequest(String username, String sessiontoken){
        this.sessiontoken = sessiontoken;
        this.username = username;
    }

    public String getSessiontoken() {
        return sessiontoken;
    }
    public String getUsername(){
        return username;
    }
}

