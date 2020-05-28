package cab302.server.Billboardserver;

import javax.swing.*;
import java.io.Serializable;

public class listUsersReply implements Serializable {
    private ListModel listOfUsers;
    public listUsersReply(ListModel listOfUsers){
        this.listOfUsers = listOfUsers;
    }

    public ListModel getListOfUsers() {
        return listOfUsers;
    }
}

