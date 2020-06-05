package cab302.server.ReplyToApplications;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Replying to client and the client get sessiontoken, permissoinslist and logged in username
 */
public class LoginReply implements Serializable {
    private  boolean loginSucceed;
    private  String sessionToken;
    private ArrayList<String> permissionsList;
    private String LoggedInUsername;

    /**
     *  Constructor setting value to get user info to an application
     * @param loggedInUsername username logged in with
     * @param sessionToken sessionToken
     * @param permissionsList permmissoins list
     * @param loginSuccess login is success or not
     */
    public LoginReply(boolean loginSuccess, String sessionToken, ArrayList<String> permissionsList, String loggedInUsername){
        this.loginSucceed = loginSuccess;
        this.sessionToken = sessionToken;
        this.permissionsList  = permissionsList;
        this.LoggedInUsername = loggedInUsername;
    }

    /**
     * @return sessionToken
     */
    public String getSessionToken(){
        return sessionToken;
    }

    /**
     * @return loginSucceed
     */
    public boolean isLoginSucceed(){
        return loginSucceed;
    }

    /**
     * @return permissionsList
     */
    public ArrayList<String> getPermissionsList() {
        return permissionsList;
    }

    /**
     * @return LoggedInUsername
     */
    public String getLoggedInUsername() {
        return LoggedInUsername;
    }

}

