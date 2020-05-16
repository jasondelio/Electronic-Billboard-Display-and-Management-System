package cab302.server.Billboardserver;

import java.io.Serializable;

public class RemoveBillboardReply implements Serializable {
    private  String billboard;



    public RemoveBillboardReply(String billboard){
        this.billboard = billboard;
    }
    public String getbillboard(){
        return billboard;
    }

}

