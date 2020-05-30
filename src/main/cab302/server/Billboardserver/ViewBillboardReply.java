package cab302.server.Billboardserver;

import javax.swing.*;
import java.io.Serializable;

public class ViewBillboardReply implements Serializable {
    private ListModel scheduledBillboard;
    private ListModel duplicatedModel;

    public ViewBillboardReply(ListModel schebillboards, ListModel duplicatedMOdel){
        this.scheduledBillboard = schebillboards;
        this.duplicatedModel = duplicatedMOdel;
    }
    public ListModel getScheduledBillboard(){
        return scheduledBillboard;
    }
    public ListModel getDuplicatedModel(){return duplicatedModel;}

}

