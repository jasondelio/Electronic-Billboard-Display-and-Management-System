package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class SessionExistRequest implements Serializable {
    private String sessionToken;

    public SessionExistRequest(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

