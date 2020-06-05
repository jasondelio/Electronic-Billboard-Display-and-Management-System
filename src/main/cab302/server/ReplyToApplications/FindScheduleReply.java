package cab302.server.ReplyToApplications;

import cab302.database.schedule.ScheduleInfo;

import java.io.Serializable;
/**
 * Replying to client and the client is able to retrieve SchduleInfo from scheduledata
 */
public class FindScheduleReply implements Serializable {
    private ScheduleInfo scheduleInfo;
    /**
     * Constructor setting value to get schedule information to an application
     * @param scheduleInfo xml content of billboard
     */
    public FindScheduleReply(ScheduleInfo scheduleInfo){
        this.scheduleInfo = scheduleInfo;
    }

    /**
     * @return scheduleInfo
     */
    public ScheduleInfo getScheduleInfo(){
        return scheduleInfo;
    }
}

