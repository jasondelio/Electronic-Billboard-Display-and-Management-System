package cab302.server.Billboardserver;

import java.io.Serializable;
import java.util.ArrayList;

public class listBillboardReply implements Serializable {
    private ArrayList<String> listofBillboards;
    public listBillboardReply(ArrayList<String> listBillboardRequest){
        this.listofBillboards = listofBillboards;
    }

    public ArrayList<String> getListofBillboards() {
        return listofBillboards;
    }
}

