package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;
import java.util.ArrayList;

public class SetUserPemmRequest implements Serializable {
    private String username;
    private String sessionToken;
    private ArrayList<String> permisssions;
    public SetUserPemmRequest(String username, String sessionToken, ArrayList<String> permisssions){
        this.sessionToken = sessionToken;
        this.username = username;
        this.permisssions = permisssions;
    }

    public String getSessionToken() {
        return sessionToken;
    }
    public String getUsername(){
        return username;
    }
    public ArrayList<String> getPermisssions(){ return  permisssions; }
}

