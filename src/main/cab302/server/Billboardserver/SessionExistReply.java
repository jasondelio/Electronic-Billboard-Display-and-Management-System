package cab302.server.Billboardserver;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Replying to client and the client will know that the session is expired or not
 */
public class SessionExistReply implements Serializable {
    private boolean loginAlready;
    private ArrayList<String> sessiontokens;

    public SessionExistReply(boolean loginAlready, ArrayList<String> sessiontokens) {
        this.loginAlready = loginAlready;
        this.sessiontokens = sessiontokens;
    }

    public boolean isLoginAlready() {
        return loginAlready;
    }
    public ArrayList<String> getSessiontokens(){
        return sessiontokens;
    }
}

