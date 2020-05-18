package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class SetPassRequest implements Serializable {
    private String username;
    private String hashedPassword;
    private String sessionToken;

    public SetPassRequest(String username, String hashedPassword, String sessionToken){
        this.hashedPassword = hashedPassword;
        this.username = username;
        this.sessionToken = sessionToken;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
    public String getUsername(){
        return username;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

