package cab302.server.Billboardserver;

import javax.swing.*;
import java.io.Serializable;

/**
 * Replying to client and the client is able to retrieve the user list model
 */
public class ListUsersReply implements Serializable {
    private ListModel listOfUsers;

    public ListUsersReply(ListModel listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public ListModel getListOfUsers() {
        return listOfUsers;
    }
}

