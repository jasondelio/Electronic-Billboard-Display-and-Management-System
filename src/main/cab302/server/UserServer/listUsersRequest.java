package cab302.server.UserServer;

import java.io.Serializable;

public class listUsersRequest implements Serializable {
    private String sessionToken;

    public listUsersRequest(String sessionToken){

        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
