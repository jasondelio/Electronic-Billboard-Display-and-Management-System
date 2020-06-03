package cab302.server.Billboardserver;

import javax.swing.*;
import java.io.Serializable;
/**
 * Replying to client and the client is able to retrieve the user list model
 */
public class ViewBillboardReply implements Serializable {
    private ListModel scheduledBillboard;
    private ListModel duplicatedModel;
    /**
     *  Constructor setting values to get schedule billboard lists to an application
     * @param schebillboards unique schedule list
     * @param duplicatedMOdel duplicated schedule list for recurring
     */
    public ViewBillboardReply(ListModel schebillboards, ListModel duplicatedMOdel){
        this.scheduledBillboard = schebillboards;
        this.duplicatedModel = duplicatedMOdel;
    }
    public ListModel getScheduledBillboard(){
        return scheduledBillboard;
    }
    public ListModel getDuplicatedModel(){return duplicatedModel;}

}

