package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;
import java.util.ArrayList;

public class CreateUsersRequest implements Serializable {
    private String sessionToken;
    private String username;
    private String password;
    private String email;
    private ArrayList<String> lists_Permissions;

    public CreateUsersRequest(String sessiontoken, String password, String username,String email, ArrayList<String> lists_permission){
        this.email = email;
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

    public String getEmail() { return email; }

    public ArrayList<String> getLists_Permissions() { return lists_Permissions; }
}

