package cab302.server.Billboardserver;

import java.io.Serializable;

/**
 * Replying to client and the client is able to know the request is success or not
 */
public class AcknowledgeReply implements Serializable {
    private String acknowledgement;


    public AcknowledgeReply(String acknowledgement){
        this.acknowledgement = acknowledgement;
    }

    public String getAcknowledgement(){
        return acknowledgement;
    }

}

