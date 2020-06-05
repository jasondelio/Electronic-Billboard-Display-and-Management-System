package cab302.server.ReplyToApplications;

import javax.swing.*;
import java.io.Serializable;
/**
 * Replying to client and client is able to retrieve the billboard list model
 */
public class ListBillboardReply implements Serializable {
    private ListModel listofBillboards;
    /**
     * Constructor setting value to get list of billboard to an application
     * @param listBillboardRequest list of billboard
     */
    public ListBillboardReply(ListModel listBillboardRequest){
        this.listofBillboards = listBillboardRequest;
    }
    /**
     * @return listofBillboards
     */
    public ListModel getListofBillboards() {
        return listofBillboards;
    }
}

