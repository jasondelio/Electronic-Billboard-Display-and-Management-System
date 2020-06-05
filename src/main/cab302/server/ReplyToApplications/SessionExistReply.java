package cab302.server.ReplyToApplications;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Replying to client and the client will know that the session is expired or not
 */
public class SessionExistReply implements Serializable {
    private  boolean loginAlready;
    private ArrayList<String> sessiontokens;
    /**
     *  Constructor setting value to get sessionTokens to an application
     * @param loginAlready true or false if sessionToken is existed
     * @param sessiontokens list of sessionTokens
     */
    public SessionExistReply(boolean loginAlready, ArrayList<String> sessiontokens){
        this.loginAlready = loginAlready;
        this.sessiontokens = sessiontokens;
    }

    /**
     * @return loginAlready
     */
    public boolean isLoginAlready(){
        return loginAlready;
    }

    /**
     * @return sessionToken
     */
    public ArrayList<String> getSessiontokens(){
        return sessiontokens;
    }
}

