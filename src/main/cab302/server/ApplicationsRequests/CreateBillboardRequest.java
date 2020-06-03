package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request creating billboard to server and the server get billboard name, billboard content and sessiontoken in username from client
 */
public class CreateBillboardRequest implements Serializable {
    private String billboardname;
    private String billboardContent;
    private String sessionToken;

    /**
     * Constructor setting values to crate a billboard
     * @param billboardname billboard's title
     * @param billboardContent billboard's content
     * @param sessionToken sessionToken
     */
    public CreateBillboardRequest(String billboardname,String billboardContent ,String sessionToken) {
        this.billboardname = billboardname;
        this.billboardContent = billboardContent;
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getBillboardContent() { return billboardContent; }

    public String getBillboardname() {
        return billboardname;
    }
}

