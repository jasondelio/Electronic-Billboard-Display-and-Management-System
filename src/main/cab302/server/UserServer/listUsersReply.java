package cab302.server.UserServer;

import java.io.Serializable;
import java.util.ArrayList;

public class listUsersReply implements Serializable {
    private ArrayList<String> listOfUsers;
    public listUsersReply(ArrayList<String> listOfUsers){
        this.listOfUsers = listOfUsers;
    }

    public ArrayList<String> getListOfUsers() {
        return listOfUsers;
    }
}

