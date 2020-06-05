package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request delating billboard to server and the server get sessiontoken and billnboard name from client
 */
public class DeleteBillboardRequest implements Serializable {
    private String billboardname;
    private String sessionToken;

    /**
     *  Constructor setting values to delete billboard in server
     * @param billboardname billboard's title
     * @param sessionToken sessiontoken
     */
    public DeleteBillboardRequest(String billboardname, String sessionToken) {
        this.billboardname = billboardname;
        this.sessionToken = sessionToken;
    }
    /**
     * @return sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * @return billboardname
     */
    public String getBillboardname() {
        return billboardname;
    }
}

