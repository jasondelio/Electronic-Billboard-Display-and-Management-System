package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request viewing billboard's schedules to server and the server get sessiontoken from client
 */
public class ViewScheduleListsRequest implements Serializable {
    private String sessionToken;

    /**
     * Constructor setting value to view billboard's schedules
     * @param sessionToken sessionToken
     */
    public ViewScheduleListsRequest(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * @return sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }

}

