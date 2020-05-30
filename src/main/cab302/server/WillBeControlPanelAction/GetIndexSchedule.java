package cab302.server.WillBeControlPanelAction;

import java.io.Serializable;

public class GetIndexSchedule implements Serializable {
    private String sessiontoken;
    private Integer index;

    public GetIndexSchedule(String sessiontoken,int index){
        this.sessiontoken =sessiontoken;
        this.index = index;
    }
    public String getSessiontoken(){
        return sessiontoken;
    }

    public Integer getIndex()  {return index;}
}
