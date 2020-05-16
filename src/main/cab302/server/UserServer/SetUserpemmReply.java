package cab302.server.UserServer;

import java.io.Serializable;
import java.util.ArrayList;

public class SetUserpemmReply implements Serializable {
    private ArrayList<String> listPermissions;


    public SetUserpemmReply(ArrayList<String> listPermissions){
        this.listPermissions = listPermissions;
    }
    public ArrayList<String> getListPermissions(){
        return listPermissions;
    }

}

