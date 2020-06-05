package cab302.server.ApplicationsRequests;

import java.io.Serializable;


/**
 * Request editing billboard to server and the server get sessiontoken, billboard Content and billnboard name from client
 */
public class EditBillboardRequest implements Serializable {
    private String billboardname;
    private String billboardContent;
    private String sessionToken;

    /**
     *  Constructor setting values to edit billboard in server
     * @param billboardname billboard's title
     * @param sessionToken sessiontoken
     * @param billboardContent billboard's content
     */
    public EditBillboardRequest(String billboardname, String billboardContent , String sessionToken) {
        this.billboardname = billboardname;
        this.billboardContent = billboardContent;
        this.sessionToken = sessionToken;
    }
    /**
     * @return sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * @return billboardContent
     */
    public String getBillboardContent() { return billboardContent; }

    /**
     * @return billboardname
     */
    public String getBillboardname() {
        return billboardname;
    }
}

