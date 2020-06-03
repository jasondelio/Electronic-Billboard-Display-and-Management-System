package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;
import java.util.Date;

/**
 * Request login to server and the server get password, logintime and username from client
 */
public class Loginrequest implements Serializable {
    private String username;
    private String password;
    private Date loginTime;

    /**
     * Constructor setting values to log in in server
     * @param username user's username
     * @param password user's password
     * @param loginTime time when user login
     */
    public Loginrequest(String username, String password, Date loginTime){
        this.password = password;
        this.username = username;
        this.loginTime = loginTime;
    }
    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public Date  getLoginTime() {
        return loginTime;
    }
}

