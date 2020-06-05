package cab302.server.ApplicationsRequests;

import java.io.Serializable;

/**
 * Request finding schedule to server and the server get sessiontoken and index client
 */
public class GetIndexSchedule implements Serializable {
    private String sessiontoken;
    private Integer index;
    /**
     *  Constructor setting values to find schedule with the index of database
     * @param index index of database
     * @param sessiontoken sessiontoken
     */
    public GetIndexSchedule(String sessiontoken,int index){
        this.sessiontoken =sessiontoken;
        this.index = index;
    }

    /**
     * @return sessionToken
     */
    public String getSessiontoken(){
        return sessiontoken;
    }

    /**
     * @return index
     */
    public Integer getIndex()  {return index;}
}
