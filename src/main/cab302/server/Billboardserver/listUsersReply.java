package cab302.server.Billboardserver;

import javax.swing.*;
import java.io.Serializable;
/**
 * Replying to client and the client is able to retrieve the user list model
 */
public class listUsersReply implements Serializable {

    private ListModel listOfUsers;

    public listUsersReply(ListModel listOfUsers){
        this.listOfUsers = listOfUsers;
    }

    public ListModel getListOfUsers() {
        return listOfUsers;
    }
}

