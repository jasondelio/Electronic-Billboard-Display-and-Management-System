package cab302.server.Billboardserver;

import java.io.Serializable;

public class CreateBillboardReply implements Serializable {
    private  String billboard;


    public CreateBillboardReply(String billboard){
        this.billboard = billboard;
    }
    public String getbillboard(){
        return billboard;
    }

}

