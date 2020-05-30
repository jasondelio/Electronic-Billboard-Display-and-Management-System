package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class DeleteUserRequest implements Serializable {
    private String username;
    private String sessiontoken;

    public DeleteUserRequest(String username, String sessiontoken) {
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

