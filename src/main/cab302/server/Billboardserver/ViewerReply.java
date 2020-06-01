package cab302.server.Billboardserver;

import cab302.database.billboard.BillboardInfo;

import java.io.Serializable;

/**
 * Replying to client and client is able to retrieve the billboard list model
 */
public class ViewerReply implements Serializable {
    private BillboardInfo billboardInfo;

    public ViewerReply(BillboardInfo billboardInfo){
        this.billboardInfo = billboardInfo;
    }

    public BillboardInfo getBillboardInfo() {
        return billboardInfo;
    }
}

