package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request confirming if the sessiontoken is not expired yet to server and the server get sessiontoken from client
 */
public class SessionExistRequest implements Serializable {
    private String sessionToken;

    /**
     * Constructor setting value to check if the session token still not expired
     * @param sessionToken
     */
    public SessionExistRequest(String sessionToken){
        this.sessionToken = sessionToken;
    }
    /**
     * @return sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }
}

