package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class ListUsersRequest implements Serializable {
    private String sessionToken;

    public ListUsersRequest(String sessionToken) {

        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}

