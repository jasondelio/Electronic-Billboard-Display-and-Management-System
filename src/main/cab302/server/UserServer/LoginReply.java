package cab302.server.UserServer;

import java.io.Serializable;

public class LoginReply implements Serializable {
    private  boolean loginSucceed;
    private  String sessionToken;


    public LoginReply(boolean loginSuccess, String sessionToken){
        this.loginSucceed = loginSuccess;
        this.sessionToken = sessionToken;
    }
    public String getSessionToken(){
        return sessionToken;
    }
    public boolean isLoginSucceed(){
        return loginSucceed;
    }
}

