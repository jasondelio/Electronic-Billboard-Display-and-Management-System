package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request getting list of billboards to server and the server get sessiontoken from client
 */
public class ListBillboardRequest implements Serializable {
    private String sessionToken;

    /**
     * Constructor setting value to list billboard in server
     * @param sessionToken sessionToken
     */
    public ListBillboardRequest(String sessionToken){
        this.sessionToken = sessionToken;
    }

    /**
     * @return sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }
}

