package cab302.server.ReplyToApplications;

import javax.swing.*;
import java.io.Serializable;
/**
 * Replying to client and the client is able to retrieve the user list model
 */
public class ListUsersReply implements Serializable {

    private ListModel listOfUsers;

    /**
     *  Constructor setting value to get list of users to an application
     * @param listOfUsers
     */
    public ListUsersReply(ListModel listOfUsers){
        this.listOfUsers = listOfUsers;
    }

    /**
     * @return listOfUsers
     */
    public ListModel getListOfUsers() {
        return listOfUsers;
    }
}

