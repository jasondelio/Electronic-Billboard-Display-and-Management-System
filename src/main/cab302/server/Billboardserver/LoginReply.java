package cab302.server.Billboardserver;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginReply implements Serializable {
    private  boolean loginSucceed;
    private  String sessionToken;
    private ArrayList<String> permissionsList;

    public LoginReply(boolean loginSuccess, String sessionToken, ArrayList<String> permissionsList){
        this.loginSucceed = loginSuccess;
        this.sessionToken = sessionToken;
        this.permissionsList  = permissionsList;
    }
    public String getSessionToken(){
        return sessionToken;
    }
    public boolean isLoginSucceed(){
        return loginSucceed;
    }

    public ArrayList<String> getPermissionsList() {
        return permissionsList;
    }
}

