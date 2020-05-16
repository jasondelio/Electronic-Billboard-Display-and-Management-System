package cab302.server.Billboardserver;

import java.io.Serializable;

public class DelateBillboardReply implements Serializable {
    private  String billboard;


    public DelateBillboardReply(String billboard){
        this.billboard = billboard;
    }
    public String getbillboard(){
        return billboard;
    }

}

