package cab302.server.Billboardserver;

import java.io.Serializable;

public class AcknowledgeReply implements Serializable {
    private String acknowledgement;


    public AcknowledgeReply(String acknowledgement){
        this.acknowledgement = acknowledgement;
    }
    public String getAcknowledgement(){
        return acknowledgement;
    }

}

