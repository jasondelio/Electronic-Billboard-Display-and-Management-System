package cab302.server.Billboardserver;

import javax.swing.*;
import java.io.Serializable;
/**
 * Replying to client and client is able to retrieve the billboard list model
 */
public class ListBillboardReply implements Serializable {
    private ListModel listofBillboards;

    public ListBillboardReply(ListModel listBillboardRequest){
        this.listofBillboards = listBillboardRequest;
    }

    public ListModel getListofBillboards() {
        return listofBillboards;
    }
}

