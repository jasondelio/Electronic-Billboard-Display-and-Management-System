package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;
/**
 * Request already logged in user's login to server and the server get sessiontoken from client
 */
public class UserLoggedInrequest implements Serializable {
    private  String sessionToken;


    public UserLoggedInrequest(String sessionToken){
        this.sessionToken = sessionToken;
    }
    public String getSessionToken(){
        return sessionToken;
    }

}

