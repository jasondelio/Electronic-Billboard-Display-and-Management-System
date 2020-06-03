package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Request setting user permissions to server and the server get sessiontoken, username, email and permissions from client
 */
public class SetUserPemmRequest implements Serializable {
    private String username;
    private String sessionToken;
    private String email;
    private ArrayList<String> permissions;

    /**
     *  Constructor setting values to remove schedule from schedules in server
     * @param sessionToken sessionToken
     * @param username username
     * @param email email
     * @param permissions permissions
     */
    public SetUserPemmRequest(String username, String sessionToken, String email, ArrayList<String> permissions){
        this.sessionToken = sessionToken;
        this.email = email;
        this.username = username;
        this.permissions = permissions;
    }

    public String getSessionToken() {
        return sessionToken;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail() {return email; }
    public ArrayList<String> getPermisssions(){ return  permissions; }
}

