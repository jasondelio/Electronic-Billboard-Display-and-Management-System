package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class Loginrequest implements Serializable {
    private  String username;
    private  String password;


    public Loginrequest(String username, String password){
        this.password = password;
        this.username = username;
    }
    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}

