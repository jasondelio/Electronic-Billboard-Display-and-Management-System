package cab302.database.RemovedSchedule;

import java.util.Set;

public interface RemovedScheduleSources {
    /**
     */
    void createSchedule(RemovedScheduleInfo s);

    void delateSchedules(String title);
    /**
     */

    RemovedScheduleInfo getSchedule(String title, String year, String month, String date, String hour, String minute, String durationHr, String durationMin);

    Set<String> titleSet();
    /**
     */
    void close();
}
