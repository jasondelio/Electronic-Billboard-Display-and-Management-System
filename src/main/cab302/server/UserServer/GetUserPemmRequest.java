package cab302.server.UserServer;

import java.io.Serializable;

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

