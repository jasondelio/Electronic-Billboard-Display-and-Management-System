package cab302.server.ApplicationsRequests;

import java.io.Serializable;
/**
 * Request already logged in user's login to server and the server get sessiontoken from client
 */
public class UserLoggedInrequest implements Serializable {
    private  String sessionToken;

    /**
     * Constructor setting value to get already logged in user information
     * @param sessionToken sessionToken
     */
    public UserLoggedInrequest(String sessionToken){
        this.sessionToken = sessionToken;
    }

    /**
     * @return sessionToken
     */
    public String getSessionToken(){
        return sessionToken;
    }

}

