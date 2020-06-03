package cab302.server.WillBeControlPanelAction;

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

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername(){
        return username;
    }
    public String getPreviousUserName() {
        return previousUserName;
    }
    public String getSessionToken() {
        return sessionToken;
    }
}

