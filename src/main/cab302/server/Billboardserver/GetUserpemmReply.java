package cab302.server.Billboardserver;

import cab302.database.user.UserInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class GetUserpemmReply implements Serializable {
    private ArrayList<String> listPermissions;
    private UserInfo u;


    public GetUserpemmReply(ArrayList<String> listPermissions, UserInfo u ){
        this.listPermissions = listPermissions;
        this.u = u;
    }
    public ArrayList<String> getListPermissions(){
        return listPermissions;
    }

    public UserInfo getU() {
        return u;
    }
}

