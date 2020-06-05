package cab302.server.ReplyToApplications;

import cab302.database.user.UserInfo;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Replying to client and the client is able to retrieve user permissions
 */
public class GetUserpemmReply implements Serializable {
    private ArrayList<String> listPermissions;
    private UserInfo u;

    /**
     * Constructor setting value to get user information to an application
     * @param u user information
     * @param listPermissions list of user permissions
     */
    public GetUserpemmReply(ArrayList<String> listPermissions, UserInfo u ){
        this.listPermissions = listPermissions;
        this.u = u;
    }
    /**
     * @return listpermissions
     */
    public ArrayList<String> getListPermissions(){
        return listPermissions;
    }

    /**
     * @return u
     */
    public UserInfo getU() {
        return u;
    }
}

