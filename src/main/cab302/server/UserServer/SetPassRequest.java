package cab302.server.UserServer;

import java.io.Serializable;

public class SetPassRequest implements Serializable {
    private String username;
    private String hashedPassword;

    public SetPassRequest(String username, String hashedPassword){
        this.hashedPassword = hashedPassword;
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
    public String getUsername(){
        return username;
    }
}

