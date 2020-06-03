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

    /**
     * Constructor setting value to get user's information to an application
     * @param loggedInUsername username already logged in
     * @param sessionToken sessionToken
     * @param permissionsList permissions list of user
     * @param loginSuccess boolean if the user login success or not
     */
    public AlreadyLoginReply(boolean loginSuccess, String sessionToken, ArrayList<String> permissionsList, String loggedInUsername){
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

