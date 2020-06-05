package cab302.server.ApplicationsRequests;

import java.io.Serializable;
/**
 * Request setting password to server and the server get sessiontoken, name, username, email, hashedpassword and previousUsername from client
 */
public class SetPassRequest implements Serializable {
    private String name;
    private String username;
    private String email;
    private String hashedPassword;
    private String sessionToken;
    private String previousUserName;
    /**
     *  Constructor setting values to remove schedule from schedules in server
     * @param name user's name
     * @param sessionToken sessionToken
     * @param username username
     * @param email email
     * @param hashedPassword hashed passoword
     * @param previousUserName previous username
     */
    public SetPassRequest(String name, String username, String email, String hashedPassword, String sessionToken, String previousUserName){
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.email = email;
        this.username = username;
        this.sessionToken = sessionToken;
        this.previousUserName = previousUserName;
    }

    /**
     * @return hashedPassword
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return username
     */
    public String getUsername(){
        return username;
    }

    /**
     * @return previousUserName
     */
    public String getPreviousUserName() {
        return previousUserName;
    }

    /**
     * @return sessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }
}

