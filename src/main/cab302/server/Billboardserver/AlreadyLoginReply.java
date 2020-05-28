package cab302.server.Billboardserver;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Replying to client and the client is able to retrieve user details
 */
public class AlreadyLoginReply implements Serializable {
    private  boolean loginSucceed;
    private  String sessionToken;
    private ArrayList<String> permissionsList;
    private String LoggedInUsername;

    public AlreadyLoginReply(boolean loginSuccess, String sessionToken, ArrayList<String> permissionsList, String loggedInUsername){
        this.loginSucceed = loginSuccess;
        this.sessionToken = sessionToken;
        this.permissionsList  = permissionsList;
        this.LoggedInUsername = loggedInUsername;
    }

    public String getSessionToken(){
        return sessionToken;
    }

    public ArrayList<String> getPermissionsList() {
        return permissionsList;
    }

    public String getLoggedInUsername() {
        return LoggedInUsername;
    }
}

