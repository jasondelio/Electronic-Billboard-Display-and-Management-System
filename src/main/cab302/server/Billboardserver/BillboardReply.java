package cab302.server.Billboardserver;

import java.io.Serializable;

public class BillboardReply implements Serializable {
    private  String billboard;


    public BillboardReply(String billboard){
        this.billboard = billboard;
    }
    public String getbillboard(){
        return billboard;
    }

}

