package cab302.server.Billboardserver;

import java.io.Serializable;

public class ScheduleBillboardReply implements Serializable {
    private  String billboard;


    public ScheduleBillboardReply(String billboard){
        this.billboard = billboard;
    }
    public String getbillboard(){
        return billboard;
    }

}

