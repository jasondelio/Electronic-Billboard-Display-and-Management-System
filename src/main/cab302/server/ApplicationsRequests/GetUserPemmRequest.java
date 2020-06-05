package cab302.server.ApplicationsRequests;

import java.io.Serializable;
/**
 * Request getting user's permissions to server and the server get sessiontoken and username from client
 */
public class GetUserPemmRequest implements Serializable {
    private String username;
    private String sessionToken;

    /**
     * Constructor setting values to get user permissions in server
     * @param sessionToken sessionToken
     * @param username new user's username
     */
    public GetUserPemmRequest (String username, String sessionToken){
        this.sessionToken = sessionToken;
        this.username = username;
    }

    /**
     * @return sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * @return username
     */
    public String getUsername(){
        return username;
    }
}

