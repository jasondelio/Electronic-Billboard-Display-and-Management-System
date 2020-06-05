package cab302.server.ReplyToApplications;

import cab302.database.billboard.BillboardInfo;

import java.io.Serializable;
/**
 * Replying to client and client is able to retrieve the billboarddInfo
 */
public class ViewerReply implements Serializable {
    private BillboardInfo billboardInfo;

    /**
     *  Constructor setting value to get billboard Information to applications
     * @param billboardInfo billboardInfo will be sent to applications
     */
    public ViewerReply(BillboardInfo billboardInfo){
        this.billboardInfo = billboardInfo;
    }

    /**
     * @return billboardInfo
     */
    public BillboardInfo getBillboardInfo() {
        return billboardInfo;
    }
}

