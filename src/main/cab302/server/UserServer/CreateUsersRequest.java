package cab302.server.UserServer;

import java.io.Serializable;
import java.util.ArrayList;

public class CreateUsersRequest implements Serializable {
    private String sessionToken;
    private String username;
    private String password;
    private ArrayList<String> lists_Permissions;

    public CreateUsersRequest(String sessiontoken, String password, String username, ArrayList<String> lists_permission){

        this.sessionToken = sessiontoken;
        this.username = username;
        this.password = password;
        this.lists_Permissions = lists_permission;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public ArrayList<String> getLists_Permissions() { return lists_Permissions; }
}

