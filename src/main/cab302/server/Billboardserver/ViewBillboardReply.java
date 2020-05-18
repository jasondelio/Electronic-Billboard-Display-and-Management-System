package cab302.server.Billboardserver;

import java.io.Serializable;
import java.util.Set;

public class ViewBillboardReply implements Serializable {
    private Set<String> billboard;


    public ViewBillboardReply(Set<String> billboard){
        this.billboard = billboard;
    }
    public Set<String> getbillboard(){
        return billboard;
    }

}

