package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request delating user to server and the server get sessiontoken and username from client
 */
public class DeleteUserRequest implements Serializable {
    private String username;
    private String sessiontoken;
    /**
     *  Constructor setting values to delete user in server
     * @param username user's name
     * @param sessiontoken sessionToken
     */
    public DeleteUserRequest(String username, String sessiontoken){
        this.sessiontoken = sessiontoken;
        this.username = username;
    }

    /**
     * @return sessionToken
     */
    public String getSessiontoken() {
        return sessiontoken;
    }

    /**
     * @return username
     */
    public String getUsername(){
        return username;
    }
}

