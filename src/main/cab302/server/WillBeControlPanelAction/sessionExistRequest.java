package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class sessionExistRequest implements Serializable {
    private String sessionToken;
    public sessionExistRequest(String sessionToken){
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

