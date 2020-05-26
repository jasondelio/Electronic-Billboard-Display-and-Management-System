package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class SetPassRequest implements Serializable {
    private String username;
    private String hashedPassword;
    private String sessionToken;
    private String previousUserName;

    public SetPassRequest(String username, String hashedPassword, String sessionToken, String previousUserName){
        this.hashedPassword = hashedPassword;
        this.username = username;
        this.sessionToken = sessionToken;
        this.previousUserName = previousUserName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
    public String getUsername(){
        return username;
    }
    public String getPreviousUserName() {
        return previousUserName;
    }
    public String getSessionToken() {
        return sessionToken;
    }
}

