package cab302.database.schedule;

import java.util.Set;

public interface ScheduleSources {
    /**
     */
    void createSchedule(ScheduleInfo s);

    /**
     */
    ScheduleInfo getSchedule(String boradtitle);

    /**
     */
    int getSize();

    /**
     */
    void deleteSchedule(String boradtitle);

    /**
     *
     * @return
     */
    ScheduleInfo editSchedule(String boradtitle);

    /**
     */
    void close();

    /**
     */
    Set<String> titleSet();

    Set<String> GetScheduleList();
}
