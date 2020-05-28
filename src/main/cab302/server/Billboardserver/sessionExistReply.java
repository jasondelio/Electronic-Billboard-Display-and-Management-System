package cab302.server.Billboardserver;

import java.io.Serializable;
import java.util.ArrayList;

public class sessionExistReply implements Serializable {
    private  boolean loginAlready;
    private ArrayList<String> sessiontokens;

    public sessionExistReply(boolean loginAlready, ArrayList<String> sessiontokens){
        this.loginAlready = loginAlready;
        this.sessiontokens = sessiontokens;
    }
    public boolean isLoginAlready(){
        return loginAlready;
    }
    public ArrayList<String> getSessiontokens(){
        return sessiontokens;
    }
}

