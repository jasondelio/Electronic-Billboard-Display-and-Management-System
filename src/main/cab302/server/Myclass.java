package cab302.server;

import java.io.Serializable;

public class Myclass implements Serializable {
    private  String username;
    private  String password;


    public Myclass(String username, String password){
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

