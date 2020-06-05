package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request getting user's list to server and the server get sessiontoken from client
 */
public class ListUsersRequest implements Serializable {
    private String sessionToken;

    /**
     * Constructor setting value to list user in server
     * @param sessionToken sessionToken
     */
    public ListUsersRequest(String sessionToken){
        this.sessionToken = sessionToken;
    }

    /**
     * @return sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }
}

