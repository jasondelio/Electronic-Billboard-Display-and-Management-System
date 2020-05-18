package cab302.server.Billboardserver;

import java.io.Serializable;
import java.util.Set;

public class listUsersReply implements Serializable {
    private Set<String> listOfUsers;
    public listUsersReply(Set<String> listOfUsers){
        this.listOfUsers = listOfUsers;
    }

    public Set<String> getListOfUsers() {
        return listOfUsers;
    }
}

