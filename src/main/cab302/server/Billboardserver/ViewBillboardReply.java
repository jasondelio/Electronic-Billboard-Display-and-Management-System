package cab302.server.Billboardserver;

import java.io.Serializable;

public class ViewBillboardReply implements Serializable {
    private  String billboard;


    public ViewBillboardReply(String billboard){
        this.billboard = billboard;
    }
    public String getbillboard(){
        return billboard;
    }

}

