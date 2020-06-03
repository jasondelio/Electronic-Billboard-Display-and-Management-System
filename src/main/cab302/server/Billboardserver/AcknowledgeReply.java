package cab302.server.Billboardserver;

import java.io.Serializable;

/**
 * Replying to client and the client is able to know the request is success or not
 */
public class AcknowledgeReply implements Serializable {
    private String acknowledgement;

    /**
     * Constructor setting value to get acknowledgement from server
     * @param acknowledgement acknowledgement to an application
     */
    public AcknowledgeReply(String acknowledgement){
        this.acknowledgement = acknowledgement;
    }
    public String getAcknowledgement(){
        return acknowledgement;
    }

}

