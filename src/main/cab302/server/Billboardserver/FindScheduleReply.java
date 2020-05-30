package cab302.server.Billboardserver;

import cab302.database.schedule.ScheduleInfo;

import java.io.Serializable;

public class FindScheduleReply implements Serializable {
    private ScheduleInfo scheduleInfo;

    public FindScheduleReply(ScheduleInfo scheduleInfo){
        this.scheduleInfo = scheduleInfo;
    }
    public ScheduleInfo getScheduleInfo(){
        return scheduleInfo;
    }
}

