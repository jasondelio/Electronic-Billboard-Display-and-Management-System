package cab302.server.Billboardserver;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginReply implements Serializable {
    private  boolean loginSucceed;
    private  String sessionToken;
    private ArrayList<String> permissionsList;
    private String LoggedInUsername;

    public LoginReply(boolean loginSuccess, String sessionToken, ArrayList<String> permissionsList, String loggedInUsername){
        this.loginSucceed = loginSuccess;
        this.sessionToken = sessionToken;
        this.permissionsList  = permissionsList;
        this.LoggedInUsername = loggedInUsername;
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

    public String getLoggedInUsername() {
        return LoggedInUsername;
    }
}

