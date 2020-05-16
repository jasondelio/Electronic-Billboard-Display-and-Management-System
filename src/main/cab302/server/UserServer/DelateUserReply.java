package cab302.server.UserServer;

import java.io.Serializable;
import java.util.ArrayList;

public class DelateUserReply implements Serializable {
    private ArrayList<String> listPermissions;


    public DelateUserReply(ArrayList<String> listPermissions){
        this.listPermissions = listPermissions;
    }
    public ArrayList<String> getListPermissions(){
        return listPermissions;
    }

}

