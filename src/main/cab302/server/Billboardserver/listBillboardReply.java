package cab302.server.Billboardserver;

import java.io.Serializable;
import java.util.Set;

public class listBillboardReply implements Serializable {
    private Set<String> listofBillboards;
    public listBillboardReply(Set<String> listBillboardRequest){
        this.listofBillboards = listBillboardRequest;
    }

    public Set<String> getListofBillboards() {
        return listofBillboards;
    }
}

