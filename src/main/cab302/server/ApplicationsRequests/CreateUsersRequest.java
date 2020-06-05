package cab302.server.ApplicationsRequests;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Request creating user to server and the server get sessiontoken, email, permissionslist ,username and password from client
 */
public class CreateUsersRequest implements Serializable {
    private String sessionToken;
    private String username;
    private String password;
    private String email;
    private ArrayList<String> lists_Permissions;

    /**
     * Constuctor setting values to create new user in server
     * @param sessiontoken sessionToken
     * @param password new user's password
     * @param username new user's username
     * @param email new user's email
     * @param lists_permission new user's list permissions
     */
    public CreateUsersRequest(String sessiontoken, String password, String username,String email, ArrayList<String> lists_permission){
        this.email = email;
        this.sessionToken = sessiontoken;
        this.username = username;
        this.password = password;
        this.lists_Permissions = lists_permission;
    }

    /**
     * @return sessionToken
     */
    public String getSessionToken() { return sessionToken; }

    /**
     * @return username
     */
    public String getUsername() { return username; }

    /**
     * @return password
     */
    public String getPassword() { return password; }

    /**
     * @return email
     */
    public String getEmail() { return email; }

    /**
     * @return lists_Permissions
     */
    public ArrayList<String> getLists_Permissions() { return lists_Permissions; }
}

