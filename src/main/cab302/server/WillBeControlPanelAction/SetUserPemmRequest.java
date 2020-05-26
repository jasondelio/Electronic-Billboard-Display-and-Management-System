package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;
import java.util.ArrayList;

public class SetUserPemmRequest implements Serializable {
    private String username;
    private String sessionToken;
    private String email;
    private ArrayList<String> permisssions;

    public SetUserPemmRequest(String username, String sessionToken, String email, ArrayList<String> permisssions){
        this.sessionToken = sessionToken;
        this.email = email;
        this.username = username;
        this.permisssions = permisssions;
    }

    public String getSessionToken() {
        return sessionToken;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail() {return email; }
    public ArrayList<String> getPermisssions(){ return  permisssions; }
}

