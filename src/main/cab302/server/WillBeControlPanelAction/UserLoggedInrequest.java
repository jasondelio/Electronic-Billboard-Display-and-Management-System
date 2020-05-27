package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class UserLoggedInrequest implements Serializable {
    private  String sessionToken;


    public UserLoggedInrequest(String sessionToken){
        this.sessionToken = sessionToken;
    }
    public String getSessionToken(){
        return sessionToken;
    }

}

