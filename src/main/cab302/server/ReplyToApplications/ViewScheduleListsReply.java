package cab302.server.ReplyToApplications;

import javax.swing.*;
import java.io.Serializable;
/**
 * Replying to client and the client is able to retrieve the schedule listModels
 */
public class ViewScheduleListsReply implements Serializable {
    private ListModel scheduledBillboard;
    private ListModel duplicatedModel;
    /**
     *  Constructor setting values to get schedule billboard lists to an application
     * @param schebillboards unique schedule list
     * @param duplicatedMOdel duplicated schedule list for recurring
     */
    public ViewScheduleListsReply(ListModel schebillboards, ListModel duplicatedMOdel){
        this.scheduledBillboard = schebillboards;
        this.duplicatedModel = duplicatedMOdel;
    }

    /**
     * @return scheduleBillboard
     */
    public ListModel getScheduledBillboard(){
        return scheduledBillboard;
    }
    /**
     * @return duplicatedModel
     */
    public ListModel getDuplicatedModel(){return duplicatedModel;}

}

